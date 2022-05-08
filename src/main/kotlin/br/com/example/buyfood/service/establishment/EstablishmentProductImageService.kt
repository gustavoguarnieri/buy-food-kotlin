package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.FileStorageFolder
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.ImageRequestDTO
import br.com.example.buyfood.model.dto.response.ImageResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.ImageEntity
import br.com.example.buyfood.model.entity.ProductEntity
import br.com.example.buyfood.model.repository.ProductImageRepository
import br.com.example.buyfood.model.repository.ProductRepository
import br.com.example.buyfood.service.FileStorageService
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.ForbiddenException

@Service
class EstablishmentProductImageService(
  private val modelMapper: ModelMapper,
  private val productRepository: ProductRepository,
  private val productImageRepository: ProductImageRepository,
  private val fileStorageService: FileStorageService,
  private val establishmentService: EstablishmentService,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  fun getProductImageList(
    establishmentId: Long,
    productId: Long,
    status: Int?
  ): List<ImageResponseDTO>? {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return if (status == null) {
      getProductImageListByEstablishmentIdAndProductId(establishment, productId)
    } else {
      productImageRepository
        .findAllByProductIdAndStatus(productId, statusValidation.getStatusIdentification(status))
        ?.filter { it?.product?.establishment?.id?.equals(establishment.id) ?: false }
        ?.mapNotNull { convertToDto(it) }
    }
  }

  fun getProductImage(establishmentId: Long, productId: Long, imageId: Long): ImageResponseDTO {
    return convertToDto(getProductImageByEstablishmentIdAndProductIdAndId(establishmentId, productId, imageId))
  }

  fun createProductImage(establishmentId: Long, productId: Long, file: MultipartFile): ImageResponseDTO {
    val productEntity = getProductByEstablishmentAndProductId(establishmentId, productId)
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return saveImage(file, establishment, productEntity)
  }

  private fun saveImage(
    file: MultipartFile,
    establishment: EstablishmentEntity,
    product: ProductEntity
  ): ImageResponseDTO {
    val downloadPath = getDownloadProductPath(establishment, product)

    val uploadFileResponse =
      product.id?.let {
        fileStorageService.saveFile(file, FileStorageFolder.PRODUCTS, it, downloadPath)
      } ?: run {
        throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
      }

    val imageEntity = fileStorageService.createImageEntity(product, uploadFileResponse)
    productImageRepository.save(imageEntity)

    return fileStorageService.createImageResponseDTO(
      imageEntity.id, uploadFileResponse, RegisterStatus.ENABLED.value
    )
  }

  fun createProductImageList(
    establishmentId: Long,
    productId: Long,
    files: Array<MultipartFile>
  ): List<ImageResponseDTO> {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    val product = getProductByEstablishmentAndProductId(establishmentId, productId)
    val downloadPath = getDownloadProductPath(establishment, product)
    val uploadFileResponse =
      fileStorageService.saveFileList(files, FileStorageFolder.PRODUCTS, productId, downloadPath)
    val imageResponseDTOList: MutableList<ImageResponseDTO> = ArrayList()
    uploadFileResponse.forEach { file ->
      val imageEntity = fileStorageService.createImageEntity(product, file)
      productImageRepository.save(imageEntity)
      imageResponseDTOList.add(
        fileStorageService.createImageResponseDTO(imageEntity.id, file, RegisterStatus.ENABLED.value)
      )
    }
    return imageResponseDTOList
  }

  fun updateProductImage(establishmentId: Long, productId: Long, imageId: Long, imageRequestDto: ImageRequestDTO) {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    validUserOwnerOfEstablishment(establishment)
    getProductImage(establishmentId, productId, imageId)
    val productEntity = getProductById(productId)
    val imageEntity = convertToEntity(imageRequestDto)
    imageEntity.apply { id = imageId; product = productEntity }
    productImageRepository.save(imageEntity)
  }

  fun deleteProductImage(establishmentId: Long, productId: Long, imageId: Long) {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    validUserOwnerOfEstablishment(establishment)
    val imageEntity = getProductImageByEstablishmentIdAndProductIdAndId(establishmentId, productId, imageId)
    imageEntity.apply { status = RegisterStatus.DISABLED.value }
    productImageRepository.save(imageEntity)
  }

  @Retryable(
    value = [Exception::class],
    maxAttempts = RETRY_MAX_ATTEMPTS,
    backoff = Backoff(delayExpression = RETRY_DELAY_TIME)
  )
  fun getDownloadProductImage(
    productId: Long,
    fileName: String,
    request: HttpServletRequest
  ): ResponseEntity<Resource> {
    return fileStorageService.downloadFile(FileStorageFolder.PRODUCTS, productId, fileName, request)
  }

  private fun getProductImageByEstablishmentIdAndProductIdAndId(
    establishmentId: Long,
    productId: Long,
    imageId: Long
  ): ImageEntity {
    val productImageRepository = productImageRepository
      .findById(imageId)
      .filter { it?.product?.id?.equals(productId) ?: false }
      .filter { it?.product?.establishment?.id?.equals(establishmentId) ?: false }

    return if (productImageRepository.isPresent) {
      productImageRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PRODUCT_IMAGE_NOT_FOUND)
    }
  }

  fun getProductById(productId: Long): ProductEntity {
    val productRepository = productRepository.findById(productId)

    return if (productRepository.isPresent) {
      productRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
    }
  }

  fun getProductByEstablishmentAndProductId(establishmentId: Long, productId: Long): ProductEntity {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return productRepository.findByEstablishmentAndId(establishment, productId) ?: run {
      throw NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)
    }
  }

  private fun getProductImageListByEstablishmentIdAndProductId(
    establishment: EstablishmentEntity,
    productId: Long
  ): List<ImageResponseDTO>? {
    return productImageRepository.findAllByProductId(productId)
      ?.filter { it?.product?.establishment?.id?.equals(establishment.id) ?: false }
      ?.mapNotNull { convertToDto(it) }
  }

  private fun getDownloadProductPath(establishment: EstablishmentEntity, product: ProductEntity): String {
    return ("/api/v1/establishments/${establishment.id}/products/${product.id}/images/download-file/")
  }

  private fun userId() = userService.userId()

  private fun validUserOwnerOfEstablishment(establishmentEntity: EstablishmentEntity) {
    if (!establishmentEntity.audit.createdBy.equals(userId())) {
      throw ForbiddenException(ErrorMessages.USER_IS_NOT_OWNER_OF_ESTABLISHMENT)
    }
  }

  private fun convertToDto(imageEntity: ImageEntity?): ImageResponseDTO {
    return modelMapper.map(imageEntity, ImageResponseDTO::class.java)
  }

  private fun convertToEntity(imageRequestDto: ImageRequestDTO): ImageEntity {
    return modelMapper.map(imageRequestDto, ImageEntity::class.java)
  }

  companion object {
    private const val RETRY_DELAY_TIME = "200"
    private const val RETRY_MAX_ATTEMPTS = 6
  }
}
