package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.FileStorageFolder
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.ImageRequestDTO
import br.com.example.buyfood.model.dto.response.ImageResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.ImageEntity
import br.com.example.buyfood.model.repository.EstablishmentImageRepository
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
class EstablishmentImageService(
  private val modelMapper: ModelMapper,
  private val establishmentImageRepository: EstablishmentImageRepository,
  private val fileStorageService: FileStorageService,
  private val establishmentService: EstablishmentService,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  fun getEstablishmentImageList(establishmentId: Long, status: Int?): List<ImageResponseDTO>? {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return if (status == null) {
      establishmentImageRepository.findAllByEstablishmentId(establishment.id)?.map { convertToDto(it) }
    } else {
      establishmentImageRepository
        .findAllByEstablishmentIdAndStatus(
          establishment.id, statusValidation.getStatusIdentification(status)
        )
        ?.map { convertToDto(it) }
    }
  }

  fun getEstablishmentImage(establishmentId: Long, imageId: Long): ImageResponseDTO {
    return convertToDto(getEstablishmentImageByEstablishmentAndImage(establishmentId, imageId))
  }

  fun createEstablishmentImage(establishmentId: Long, file: MultipartFile): ImageResponseDTO {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return saveImage(file, establishment)
  }

  private fun saveImage(file: MultipartFile, establishment: EstablishmentEntity): ImageResponseDTO {
    val downloadPath = getDownloadEstablishmentPath(establishment)

    val uploadFileResponse = establishment.id?.let {
      fileStorageService.saveFile(
        file, FileStorageFolder.ESTABLISHMENTS, it, downloadPath
      )
    } ?: run {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_NOT_FOUND)
    }

    val imageEntity = fileStorageService.createImageEntity(establishment, uploadFileResponse)
    establishmentImageRepository.save(imageEntity)

    return fileStorageService.createImageResponseDTO(
      imageEntity.id, uploadFileResponse, RegisterStatus.ENABLED.value
    )
  }

  fun createEstablishmentImageList(establishmentId: Long, files: Array<MultipartFile>): List<ImageResponseDTO> {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    val downloadPath = getDownloadEstablishmentPath(establishment)
    val uploadFileResponse = fileStorageService.saveFileList(
      files, FileStorageFolder.ESTABLISHMENTS, establishmentId, downloadPath
    )
    val imageResponseDTOList: MutableList<ImageResponseDTO> = ArrayList()
    uploadFileResponse.forEach { file ->
      val imageEntity = fileStorageService.createImageEntity(establishment, file)
      establishmentImageRepository.save(imageEntity)
      imageResponseDTOList.add(
        fileStorageService.createImageResponseDTO(
          imageEntity.id, file, RegisterStatus.ENABLED.value
        )
      )
    }
    return imageResponseDTOList
  }

  fun updateEstablishmentImage(establishmentId: Long, imageId: Long, imageRequestDto: ImageRequestDTO) {
    getEstablishmentImage(establishmentId, imageId)
    val establishmentEntity = establishmentService.getEstablishmentById(establishmentId)
    validUserOwnerOfEstablishment(establishmentEntity)
    val imageEntity = convertToEntity(imageRequestDto)
    imageEntity.apply { id = imageId; establishment = establishmentEntity }
    establishmentImageRepository.save(imageEntity)
  }

  fun deleteEstablishmentImage(establishmentId: Long, imageId: Long) {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    validUserOwnerOfEstablishment(establishment)
    val imageEntity = getEstablishmentImageByEstablishmentAndImage(establishmentId, imageId)
    imageEntity.apply { status = RegisterStatus.DISABLED.value }
    establishmentImageRepository.save(imageEntity)
  }

  @Retryable(
    value = [Exception::class],
    maxAttempts = RETRY_MAX_ATTEMPTS,
    backoff = Backoff(delayExpression = RETRY_DELAY_TIME)
  )
  fun getDownloadEstablishmentImage(
    establishmentId: Long,
    fileName: String,
    request: HttpServletRequest
  ): ResponseEntity<Resource> {
    return fileStorageService.downloadFile(
      FileStorageFolder.ESTABLISHMENTS, establishmentId, fileName, request
    )
  }

  private fun getEstablishmentImageByEstablishmentAndImage(establishmentId: Long, imageId: Long): ImageEntity {
    val imageEntity = establishmentImageRepository.findByIdAndEstablishmentId(imageId, establishmentId)

    return imageEntity ?: run {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_IMAGE_NOT_FOUND)
    }
  }

  private fun getDownloadEstablishmentPath(establishment: EstablishmentEntity): String {
    return "/api/v1/establishments/" + establishment.id.toString() + "/images/download-file/"
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
