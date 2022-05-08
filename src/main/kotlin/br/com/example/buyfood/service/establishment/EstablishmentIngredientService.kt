package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.IngredientRequestDTO
import br.com.example.buyfood.model.dto.response.IngredientResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.IngredientEntity
import br.com.example.buyfood.model.entity.ProductEntity
import br.com.example.buyfood.model.repository.IngredientRepository
import br.com.example.buyfood.model.repository.ProductRepository
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import javax.ws.rs.ForbiddenException

@Service
class EstablishmentIngredientService(
  private val modelMapper: ModelMapper,
  private val productRepository: ProductRepository,
  private val ingredientRepository: IngredientRepository,
  private val establishmentService: EstablishmentService,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  fun getIngredientList(
    establishmentId: Long,
    productId: Long,
    status: Int?
  ): List<IngredientResponseDTO>? {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return if (status == null) {
      ingredientRepository.findAllByProductId(productId)
        ?.filter { it?.product?.establishment?.id?.equals(establishment.id) ?: false }
        ?.mapNotNull { it?.let { convertToDto(it) } }
    } else {
      ingredientRepository.findAllByProductIdAndStatus(
        productId,
        statusValidation.getStatusIdentification(status)
      )
        ?.filter { it?.product?.establishment?.id?.equals(establishment.id) ?: false }
        ?.mapNotNull { it?.let { convertToDto(it) } }
    }
  }

  fun getIngredient(establishmentId: Long, productId: Long, ingredientId: Long): IngredientResponseDTO {
    return convertToDto(getIngredientByEstablishmentIdAndProductIdAndId(establishmentId, productId, ingredientId))
  }

  fun createIngredient(
    establishmentId: Long,
    productId: Long,
    ingredientRequestDTO: IngredientRequestDTO
  ): IngredientResponseDTO {
    val productEntity = getProductByEstablishmentIdAndProductId(establishmentId, productId)
    val convertedIngredientEntity = convertToEntity(ingredientRequestDTO)
    convertedIngredientEntity.apply { product = productEntity }
    return convertToDto(ingredientRepository.save(convertedIngredientEntity))
  }

  fun updateIngredient(
    establishmentId: Long,
    productId: Long,
    ingredientId: Long,
    ingredientRequestDTO: IngredientRequestDTO
  ) {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    validUserOwnerOfEstablishment(establishment)
    val productEntity = getProductByEstablishmentAndProductId(establishment, productId)
    val convertedIngredientEntity = convertToEntity(ingredientRequestDTO)
    convertedIngredientEntity.apply { id = ingredientId; product = productEntity }
    ingredientRepository.save(convertedIngredientEntity)
  }

  fun deleteIngredient(establishmentId: Long, ingredientId: Long) {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    validUserOwnerOfEstablishment(establishment)
    val ingredientEntity = getIngredientById(ingredientId)
    ingredientEntity.apply { status = RegisterStatus.DISABLED.value }
    ingredientRepository.save(ingredientEntity)
  }

  fun getIngredientById(ingredientId: Long): IngredientEntity {
    val ingredientEntity = ingredientRepository.findById(ingredientId)

    return if (ingredientEntity.isPresent) {
      ingredientEntity.get()
    } else {
      throw NotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND)
    }
  }

  fun getProductByEstablishmentIdAndProductId(
    establishmentId: Long,
    productId: Long
  ): ProductEntity {
    val establishment = establishmentService.getEstablishmentById(establishmentId)

    return productRepository.findByEstablishmentAndId(establishment, productId) ?: run {
      throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
    }
  }

  fun getProductByEstablishmentAndProductId(
    establishment: EstablishmentEntity,
    productId: Long
  ): ProductEntity {
    return productRepository.findByEstablishmentAndId(establishment, productId) ?: run {
      throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
    }
  }

  private fun getIngredientByEstablishmentIdAndProductIdAndId(
    establishmentId: Long,
    productId: Long,
    ingredientId: Long
  ): IngredientEntity {

    val ingredientEntity = ingredientRepository.findById(ingredientId)
      .filter { it?.product?.id?.equals(productId) ?: false }
      .filter { it?.product?.establishment?.id?.equals(establishmentId) ?: false }

    return if (ingredientEntity.isPresent) {
      ingredientEntity.get()
    } else {
      throw NotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND)
    }
  }

  private fun userId() = userService.userId()

  private fun validUserOwnerOfEstablishment(establishmentEntity: EstablishmentEntity) {
    if (!establishmentEntity.audit.createdBy.equals(userId())) {
      throw ForbiddenException(ErrorMessages.USER_IS_NOT_OWNER_OF_ESTABLISHMENT)
    }
  }

  private fun convertToDto(ingredientEntity: IngredientEntity): IngredientResponseDTO {
    return modelMapper.map(ingredientEntity, IngredientResponseDTO::class.java)
  }

  private fun convertToEntity(ingredientRequestDTO: IngredientRequestDTO): IngredientEntity {
    return modelMapper.map(ingredientRequestDTO, IngredientEntity::class.java)
  }
}
