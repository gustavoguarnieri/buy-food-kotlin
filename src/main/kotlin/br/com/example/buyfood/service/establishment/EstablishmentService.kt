package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.EstablishmentRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentCategoryEntity
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.repository.EstablishmentCategoryRepository
import br.com.example.buyfood.model.repository.EstablishmentRepository
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import javax.ws.rs.ForbiddenException

@Service
class EstablishmentService(
  private val modelMapper: ModelMapper,
  private val establishmentRepository: EstablishmentRepository,
  private val establishmentCategoryRepository: EstablishmentCategoryRepository,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  fun getEstablishmentList(status: Int?): List<EstablishmentResponseDTO>? {
    return if (status == null) {
      establishmentRepository.findAll().map { convertToDto(it) }
    } else {
      establishmentRepository
        .findAllByStatus(statusValidation.getStatusIdentification(status))?.map { convertToDto(it) }
    }
  }

  fun getEstablishment(id: Long): EstablishmentResponseDTO {
    val establishmentRepository = establishmentRepository.findById(id)
      .map { convertToDto(it) }

    return if (establishmentRepository.isPresent) {
      establishmentRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_NOT_FOUND)
    }
  }

  fun getMyEstablishmentList(status: Int?): List<EstablishmentResponseDTO>? {
    return if (status == null) {
      establishmentRepository
        .findAllByAuditCreatedBy(userService.userId())
        ?.mapNotNull { convertToDto(it) }
    } else {
      establishmentRepository
        .findAllByAuditCreatedByAndStatus(
          userService.userId(),
          statusValidation.getStatusIdentification(status)
        )
        ?.mapNotNull { convertToDto(it) }
    }
  }

  fun createEstablishment(
    establishmentRequestDto: EstablishmentRequestDTO
  ): EstablishmentResponseDTO {
    establishmentRequestDto.category?.id?.let { getEstablishmentCategoryById(it) }
    val convertedEstablishmentEntity = convertToEntity(establishmentRequestDto)
    return convertToDto(establishmentRepository.save(convertedEstablishmentEntity))
  }

  fun updateEstablishment(id: Long, establishmentRequestDto: EstablishmentRequestDTO) {
    val establishmentEntity = getEstablishmentById(id)
    val convertedEstablishmentEntity = convertToEntity(establishmentRequestDto)
    if (convertedEstablishmentEntity.category == null) {
      convertedEstablishmentEntity.category = establishmentEntity.category
    }
    convertedEstablishmentEntity.audit = establishmentEntity.audit
    validUserOwnerOfEstablishment(convertedEstablishmentEntity)
    convertedEstablishmentEntity.id = id
    establishmentRepository.save(convertedEstablishmentEntity)
  }

  fun deleteEstablishment(id: Long) {
    val establishmentEntity = getEstablishmentById(id)
    establishmentEntity.apply { status = RegisterStatus.DISABLED.value }
    establishmentRepository.save(establishmentEntity)
  }

  fun getEstablishmentById(id: Long): EstablishmentEntity {
    val establishmentRepository = establishmentRepository.findById(id)

    return if (establishmentRepository.isPresent) {
      establishmentRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_NOT_FOUND)
    }
  }

  fun getEstablishmentCategoryById(id: Long): EstablishmentCategoryEntity? {
    return establishmentCategoryRepository
      .findById(id)
      .orElseThrow { NotFoundException(ErrorMessages.ESTABLISHMENT_CATEGORY_NOT_FOUND) }
  }

  private fun userId() = userService.userId()

  private fun validUserOwnerOfEstablishment(establishmentEntity: EstablishmentEntity) {
    if (!establishmentEntity.audit.createdBy.equals(userId())) {
      throw ForbiddenException(ErrorMessages.USER_IS_NOT_OWNER_OF_ESTABLISHMENT)
    }
  }

  private fun convertToDto(establishmentEntity: EstablishmentEntity?): EstablishmentResponseDTO {
    return modelMapper.map(establishmentEntity, EstablishmentResponseDTO::class.java)
  }

  private fun convertToEntity(establishmentRequestDto: EstablishmentRequestDTO): EstablishmentEntity {
    return modelMapper.map(establishmentRequestDto, EstablishmentEntity::class.java)
  }
}
