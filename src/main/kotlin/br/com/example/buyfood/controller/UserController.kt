package br.com.example.buyfood.controller

import br.com.example.buyfood.model.dto.request.UserCreateRequestDTO
import br.com.example.buyfood.model.dto.request.UserSigninRequestDTO
import br.com.example.buyfood.model.dto.request.UserUpdateRequestDTO
import br.com.example.buyfood.model.dto.response.UserCreateResponseDTO
import br.com.example.buyfood.model.dto.response.UserResponseDTO
import br.com.example.buyfood.service.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.keycloak.representations.AccessTokenResponse
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@CrossOrigin
@RestController
@RequestMapping(value = ["/api/v1/users"])
class UserController(private val userService: UserService) {

  private val log = KotlinLogging.logger {}

  @Secured("ROLE_ESTABLISHMENT", "ROLE_USER", "ROLE_ADMIN")
  @GetMapping("/{userId}")
  @ApiOperation(value = "Returns the informed user")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed user",
        response = UserResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getUser(@PathVariable("userId") userId: @Valid @NotBlank String): UserResponseDTO? {
    log.info("getUser: starting to consult user by userId=$userId")
    return userService.getUser(userId).also {
      log.info("getUser: finished to consult user by userId=$userId")
    }
  }

  @ApiOperation(value = "Create a new user")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created user",
        response = UserCreateResponseDTO::class
      ),
      ApiResponse(code = 400, message = "Bad Request"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  @PostMapping(path = ["/create"])
  fun createUser(@RequestBody userCreateRequestDTO: @Valid UserCreateRequestDTO): UserCreateResponseDTO? {
    log.info(
      "createUser: starting create user firstname=${userCreateRequestDTO.firstName} " +
        "email=${userCreateRequestDTO.email}"
    )
    return userService.createUser(userCreateRequestDTO).also {
      log.info(
        "createUser: finishing create user firstname=${userCreateRequestDTO.firstName} " +
          "email=${userCreateRequestDTO.email}"
      )
    }
  }

  @ApiOperation(value = "Signin")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "OK", response = AccessTokenResponse::class),
      ApiResponse(code = 400, message = "Bad Request"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  @PostMapping(path = ["/signin"])
  fun signin(@RequestBody userSignin: @Valid UserSigninRequestDTO): AccessTokenResponse? {
    log.info("signin: starting signin user email=${userSignin.email}")
    return userService.signin(userSignin).also {
      log.info("signin: finishing signin user email=${userSignin.email}")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_USER", "ROLE_ADMIN")
  @PutMapping("/{userId}")
  @ApiOperation(value = "Update user")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated user"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateUser(
    @PathVariable("userId") userId: @Valid @NotBlank String,
    @RequestBody userUpdateRequestDto: @Valid UserUpdateRequestDTO
  ) {
    log.info("updateUser: starting update user userId=$userId")
    userService.updateCustomUser(userId, userUpdateRequestDto).also {
      log.info("updateUser: finished update user userId=$userId")
    }
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{userId}")
  @ApiOperation(value = "Delete user")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted user"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteUser(@PathVariable("userId") userId: @Valid @NotBlank String) {
    log.info("deleteUser: starting delete user userId=$userId")
    userService.deleteCustomUser(userId).also { log.info("deleteUser: finished delete user userId=$userId") }
  }
}
