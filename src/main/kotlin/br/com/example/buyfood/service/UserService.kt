package br.com.example.buyfood.service

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.constant.User.UNKNOWN_USER
import br.com.example.buyfood.enums.Role
import br.com.example.buyfood.exception.BusinessException
import br.com.example.buyfood.exception.ConflictException
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.UserCreateRequestDTO
import br.com.example.buyfood.model.dto.request.UserSigninRequestDTO
import br.com.example.buyfood.model.dto.request.UserUpdateRequestDTO
import br.com.example.buyfood.model.dto.response.UserCreateResponseDTO
import br.com.example.buyfood.model.dto.response.UserResponseDTO
import br.com.example.buyfood.model.entity.UserEntity
import br.com.example.buyfood.model.repository.UserRepository
import mu.KotlinLogging
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.authorization.client.AuthzClient
import org.keycloak.authorization.client.Configuration
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class UserService(
  @Value("\${keycloak.realm}") private val realm: String? = null,
  @Value("\${keycloak.auth-server-url}") private val authServerUrl: String? = null,
  @Value("\${keycloak.resource}") private val clientId: String? = null,
  @Value("\${keycloak.credentials.secret}") private val clientSecret: String? = null,
  @Value("\${keycloak-custom.admin-user}") private val adminUser: String? = null,
  @Value("\${keycloak-custom.admin-password}") private val adminPass: String? = null,
  private val modelMapper: ModelMapper,
  private val userRepository: UserRepository
) {

  private val log = KotlinLogging.logger {}

  fun getUser(userId: String?): UserResponseDTO {
    val userEntity = userRepository.findByUserId(userId)

    userEntity?.let {
      return convertToDto(userEntity)
    } ?: run {
      throw NotFoundException(ErrorMessages.USER_NOT_FOUND)
    }
  }

  fun createUser(userCreateRequestDto: UserCreateRequestDTO): UserCreateResponseDTO {
    val userEntity = saveCustomUser(userCreateRequestDto)
    val role = userCreateRequestDto.role?.name?.lowercase()
    val keycloak = getKeycloakBuilder(adminUser, adminPass)
    val user = UserRepresentation()
    user.apply {
      isEnabled = true
      username = userCreateRequestDto.email
      firstName = userCreateRequestDto.firstName
      lastName = userCreateRequestDto.lastName
      email = userCreateRequestDto.email
    }
    val realmResource = keycloak.realm(realm)
    val usersResource = realmResource.users()
    val userCreateResponseDto = UserCreateResponseDTO()
    try {
      usersResource.create(user).use { response ->
        userCreateResponseDto.statusCode = response.status
        userCreateResponseDto.status = response.statusInfo.toString()
        if (response.status == HttpStatus.CREATED.value()) {
          val userId = CreatedResponseUtil.getCreatedId(response)

          userCreateResponseDto.userId = userId

          log.info("createUser: Created user userId=$userId, userMail=${userCreateRequestDto.email}")
          val passwordCred = KeycloakService().getCredentialRepresentation(userCreateRequestDto.password)
          val userResource = usersResource[userId]
          userResource.resetPassword(passwordCred)
          insertNewRole(role, realmResource, userResource)
          userEntity.userId = userId
          userEntity.audit.createdBy = userId
          userRepository.save(userEntity)
        } else {
          deleteCustomUser(userEntity.userId)
          log.warn(
            "createUser: Unexpected status code for userMail=${user.email}, " +
              "status=${response.status.toString() + "-" + response.statusInfo.toString()}"
          )
        }
      }
    } catch (ex: Exception) {
      log.error("createUser: An error occurred when creating the user=${user.username}", ex)
    }
    return userCreateResponseDto
  }

  private fun insertNewRole(
    newRole: String?,
    realmResource: RealmResource,
    userResource: UserResource
  ) {
    if (newRole.isNullOrBlank()) {
      return
    }

    val realmRoleUser: RoleRepresentation
    try {
      log.info("insertNewRole: Available roles: ${realmResource.roles().list()}")
      realmRoleUser = realmResource.roles()[newRole].toRepresentation()
      userResource.roles().realmLevel().add(listOf(realmRoleUser))
      removeOldRoles(newRole, realmResource, userResource)
    } catch (ex: Exception) {
      log.error(
        "insertNewRole: An error occurred when get role=${newRole.lowercase(Locale.getDefault())}", ex
      )
    }
  }

  private fun removeOldRoles(
    newRole: String,
    realmResource: RealmResource,
    userResource: UserResource
  ) {
    val roleRepresentationList: MutableList<RoleRepresentation> = ArrayList()
    Role.stream()
      .filter { i -> i.name != newRole.uppercase(Locale.getDefault()) }
      .forEach { i ->
        val realmRoleUser = realmResource.roles()[i.name].toRepresentation()
        roleRepresentationList.add(realmRoleUser)
      }
    userResource.roles().realmLevel().remove(roleRepresentationList)
  }

  private fun getKeycloakBuilder(user: String?, pass: String?): Keycloak {
    return KeycloakBuilder.builder()
      .serverUrl(authServerUrl)
      .grantType(OAuth2Constants.PASSWORD)
      .realm(realm)
      .clientId(clientId)
      .clientSecret(clientSecret)
      .username(user)
      .password(pass)
      .resteasyClient(
        ResteasyClientBuilder().connectionPoolSize(KEYCLOAK_CONNECTION_POOL_SIZE).build()
      )
      .build()
  }

  fun signin(userSignin: UserSigninRequestDTO): AccessTokenResponse? {
    val clientCredentials = clientCredentials()
    var response: AccessTokenResponse? = null
    try {
      val configuration = Configuration(authServerUrl, realm, clientId, clientCredentials, null)
      val authzClient = AuthzClient.create(configuration)
      response = authzClient.obtainAccessToken(userSignin.email, userSignin.password)
    } catch (ex: Exception) {
      log.error("signin: An error occurred when signing user=${userSignin.email}", ex)
    }
    return response
  }

  private fun clientCredentials(): Map<String, Any?> {
    val clientCredentials: MutableMap<String, Any?> = HashMap()
    clientCredentials["secret"] = clientSecret
    clientCredentials["grant_type"] = CredentialRepresentation.PASSWORD
    return clientCredentials
  }

  fun userId() = KeycloakService().getKeycloakClaims()?.get("user_id").toString().ifBlank { UNKNOWN_USER }

  private fun saveCustomUser(userCreateRequestDto: UserCreateRequestDTO): UserEntity {
    val user = userRepository.findByEmail(userCreateRequestDto.email)

    user?.let {
      log.warn("saveCustomUser: Duplicated resource, this email=${userCreateRequestDto.email} already exist")
      throw ConflictException("Duplicated resource, this email already exist")
    }

    return try {
      val userEntity = convertToEntity(userCreateRequestDto)
      userRepository.save(userEntity)
    } catch (ex: Exception) {
      log.error("saveCustomUser: An error occurred when save user=${userCreateRequestDto.email}", ex)
      throw BusinessException(ex.message)
    }
  }

  fun updateCustomUser(userId: String?, userUpdateRequestDto: UserUpdateRequestDTO) {
    val userEntity = userRepository.findByUserId(userId)

    userEntity?.let {
      try {
        it.userId = userId
        it.email = userUpdateRequestDto.email
        it.firstName = userUpdateRequestDto.firstName
        it.lastName = userUpdateRequestDto.lastName
        it.nickName = userUpdateRequestDto.nickName
        it.phone = userUpdateRequestDto.phone
        it.audit.lastUpdatedBy = userId
        userRepository.save(it)
      } catch (ex: Exception) {
        log.error("updateCustomUser: An error occurred when update userId=$userId", ex)
        throw BusinessException(ex.message)
      }
    }

    try {
      val keycloak = getKeycloakBuilder(adminUser, adminPass)
      val realmResource = keycloak.realm(realm)
      val usersResource = realmResource.users()
      val userResource = usersResource[userId]
      val user = userResource.toRepresentation()
      user.apply { firstName = userUpdateRequestDto.firstName; lastName = userUpdateRequestDto.lastName }
      val passwordCred: CredentialRepresentation
      if (!userUpdateRequestDto.password.isNullOrBlank()) {
        passwordCred = KeycloakService().getCredentialRepresentation(userUpdateRequestDto.password)
        userResource.resetPassword(passwordCred)
      }
      usersResource[userId].update(user)
      insertNewRole(userUpdateRequestDto.role?.lowercase(), realmResource, userResource)
    } catch (ex: Exception) {
      log.error("updateCustomUser: An error occurred when update keycloak user=${userEntity?.email}", ex)
      throw BusinessException(ex.message)
    }
  }

  fun deleteCustomUser(userId: String?) {
    val userEntity = userRepository.findByUserId(userId)

    userEntity?.let {
      try {
        it.status = 0
        userRepository.save(it)
      } catch (ex: Exception) {
        log.error("deleteCustomUser: An error occurred when update userId=$userId", ex)
        throw BusinessException(ex.message)
      }
      try {
        val keycloak = getKeycloakBuilder(adminUser, adminPass)
        val realmResource = keycloak.realm(realm)
        val usersResource = realmResource.users()
        val user = usersResource[userEntity.userId].toRepresentation()
        user.isEnabled = false
        usersResource[userEntity.userId].update(user)
      } catch (ex: Exception) {
        log.error("deleteCustomUser: An error occurred when update keycloak user=${userEntity.email}", ex)
        throw BusinessException(ex.message)
      }
    } ?: run {
      throw NotFoundException(ErrorMessages.USER_NOT_FOUND)
    }
  }

  private fun convertToDto(userEntity: UserEntity?): UserResponseDTO {
    return modelMapper.map(userEntity, UserResponseDTO::class.java)
  }

  private fun convertToEntity(userCreateRequestDto: Any): UserEntity {
    return modelMapper.map(userCreateRequestDto, UserEntity::class.java)
  }

  companion object {
    private const val KEYCLOAK_CONNECTION_POOL_SIZE = 15
  }
}
