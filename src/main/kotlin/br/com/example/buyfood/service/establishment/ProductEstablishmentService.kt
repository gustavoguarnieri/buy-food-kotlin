package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.ProductRequestDTO
import br.com.example.buyfood.model.dto.response.ProductResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.ProductEntity
import br.com.example.buyfood.model.repository.ProductRepository
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import javax.ws.rs.ForbiddenException

@Service
class ProductEstablishmentService(
  private val modelMapper: ModelMapper,
  private val productRepository: ProductRepository,
  private val establishmentService: EstablishmentService,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  fun getProductListByEstablishment(
    establishmentId: Long,
    status: Int?
  ): List<ProductResponseDTO>? {
    val establishment = establishmentService.getEstablishmentById(establishmentId)

    return status?.let { statusNotNullable ->
      productRepository.findAllByEstablishmentAndStatus(
        establishment, statusValidation.getStatusIdentification(statusNotNullable)
      )
        ?.map { convertToDto(it) }
    } ?: run {
      productRepository.findAllByEstablishment(establishment)?.map { convertToDto(it) }
    }
  }

  fun getProduct(establishmentId: Long, productId: Long): ProductResponseDTO {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    val productRepository = productRepository.findByEstablishmentAndId(establishment, productId)

    productRepository?.let {
      return convertToDto(it)
    } ?: run {
      throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
    }
  }

  fun createProduct(
    establishmentId: Long,
    productRequestDto: ProductRequestDTO
  ): ProductResponseDTO {
    val establishmentEntity = establishmentService.getEstablishmentById(establishmentId)
    val convertedProductEntity = convertToEntity(productRequestDto)
    convertedProductEntity.apply { establishment = establishmentEntity }
    return convertToDto(productRepository.save(convertedProductEntity))
  }

  fun updateProduct(
    establishmentId: Long?,
    productId: Long?,
    productRequestDto: ProductRequestDTO
  ) {
    val establishmentEntity = establishmentService.getEstablishmentById(establishmentId!!)
    validUserOwnerOfEstablishment(establishmentEntity)
    val convertedProductEntity = convertToEntity(productRequestDto)
    convertedProductEntity.apply { id = productId; establishment = establishmentEntity }
    productRepository.save(convertedProductEntity)
  }

  fun deleteProduct(establishmentId: Long?, productId: Long) {
    val establishment = establishmentService.getEstablishmentById(establishmentId!!)
    validUserOwnerOfEstablishment(establishment)
    val productEntity = getProductById(productId)
    productEntity.apply { status = RegisterStatus.DISABLED.value }
    productRepository.save(productEntity)
  }

  fun getProductById(productId: Long): ProductEntity {
    val productRepository = productRepository.findById(productId)

    return if (productRepository.isPresent) {
      productRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
    }
  }

  private fun validUserOwnerOfEstablishment(establishmentEntity: EstablishmentEntity) {
    if (!establishmentEntity.audit.createdBy.equals(userService.userId())) {
      throw ForbiddenException(ErrorMessages.USER_IS_NOT_OWNER_OF_ESTABLISHMENT)
    }
  }

  private fun convertToDto(productEntity: ProductEntity?): ProductResponseDTO {
    return modelMapper.map(productEntity, ProductResponseDTO::class.java)
  }

  private fun convertToEntity(productRequestDto: ProductRequestDTO): ProductEntity {
    return modelMapper.map(productRequestDto, ProductEntity::class.java)
  }
}
