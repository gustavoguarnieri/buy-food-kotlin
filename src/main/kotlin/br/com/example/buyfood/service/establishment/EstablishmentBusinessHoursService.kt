package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.BadRequestException
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.BusinessHoursRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentBusinessHoursResponseDTO
import br.com.example.buyfood.model.entity.BusinessHoursEntity
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.repository.BusinessHoursRepository
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import javax.ws.rs.ForbiddenException

@Service
class EstablishmentBusinessHoursService(
  private val modelMapper: ModelMapper,
  private val businessHoursRepository: BusinessHoursRepository,
  private val establishmentService: EstablishmentService,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  private val log = KotlinLogging.logger {}

  fun getBusinessHoursList(establishmentId: Long, status: Int?): List<EstablishmentBusinessHoursResponseDTO>? {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    return if (status == null) {
      businessHoursRepository.findAllByEstablishment(establishment)?.map { convertToDto(it) }
    } else {
      businessHoursRepository
        .findAllByEstablishmentAndStatus(establishment, statusValidation.getStatusIdentification(status))
        ?.map { convertToDto(it) }
    }
  }

  fun getBusinessHours(establishmentId: Long, businessHoursId: Long): EstablishmentBusinessHoursResponseDTO {
    val establishment = establishmentService.getEstablishmentById(establishmentId)
    val businessHoursRepository = businessHoursRepository.findByEstablishmentAndId(establishment, businessHoursId)

    businessHoursRepository?.let {
      return convertToDto(it)
    } ?: run {
      throw NotFoundException("Business hours not found")
    }
  }

  fun getMyBusinessHoursList(status: Int?): List<EstablishmentBusinessHoursResponseDTO>? {
    return if (status == null) {
      businessHoursRepository
        .findAllByAuditCreatedBy(userService.userId())
        ?.map { convertToDto(it) }
    } else {
      businessHoursRepository
        .findAllByAuditCreatedByAndStatus(
          userService.userId(),
          statusValidation.getStatusIdentification(status)
        )
        ?.map { convertToDto(it) }
    }
  }

  fun createBusinessHours(
    establishmentId: Long,
    businessHoursRequestDto: BusinessHoursRequestDTO
  ): EstablishmentBusinessHoursResponseDTO {
    val establishmentEntity = establishmentService.getEstablishmentById(establishmentId)
    if (establishmentExists(establishmentEntity)) {
      log.warn("createBusinessHours: establishment already exist establishmentId=$establishmentId")
      throw BadRequestException("Establishment already exist")
    }
    val convertedBusinessHoursEntity = convertToEntity(businessHoursRequestDto)
    convertedBusinessHoursEntity.apply { establishment = establishmentEntity }

    val businessHoursEntity = businessHoursRepository.save(convertedBusinessHoursEntity)
    return convertToDto(businessHoursEntity)
  }

  fun updateBusinessHours(
    establishmentId: Long,
    businessHoursId: Long,
    businessHoursRequestDto: BusinessHoursRequestDTO
  ) {
    val establishmentEntity = establishmentService.getEstablishmentById(establishmentId)
    isValidUserOwnerOfEstablishment(establishmentEntity)
    val convertedBusinessHoursEntity = convertToEntity(businessHoursRequestDto)
    convertedBusinessHoursEntity.apply { id = businessHoursId; establishment = establishmentEntity }
    businessHoursRepository.save(convertedBusinessHoursEntity)
  }

  fun deleteBusinessHours(establishmentId: Long, businessHoursId: Long) {
    val establishmentEntity = establishmentService.getEstablishmentById(establishmentId)
    isValidUserOwnerOfEstablishment(establishmentEntity)
    val businessHoursEntity = getBusinessHoursById(businessHoursId)
    businessHoursEntity.apply { status = RegisterStatus.DISABLED.value }
    businessHoursRepository.save(businessHoursEntity)
  }

  fun getBusinessHoursById(businessHoursId: Long): BusinessHoursEntity {
    val businessHoursRepository = businessHoursRepository.findById(businessHoursId)

    return if (businessHoursRepository.isPresent) {
      businessHoursRepository.get()
    } else {
      throw NotFoundException("Business hours not found")
    }
  }

  private fun establishmentExists(establishment: EstablishmentEntity): Boolean {
    val businessHoursRepository = businessHoursRepository.findByEstablishment(establishment)
    return businessHoursRepository.let { true }
  }

  private fun isValidUserOwnerOfEstablishment(establishmentEntity: EstablishmentEntity) {
    if (!establishmentEntity.audit.createdBy.equals(userService.userId())) {
      throw ForbiddenException(ErrorMessages.USER_IS_NOT_OWNER_OF_ESTABLISHMENT)
    }
  }

  private fun convertToDto(businessEntity: BusinessHoursEntity?): EstablishmentBusinessHoursResponseDTO {
    return modelMapper.map(businessEntity, EstablishmentBusinessHoursResponseDTO::class.java)
  }

  private fun convertToEntity(businessHoursRequestDto: Any): BusinessHoursEntity {
    return modelMapper.map(businessHoursRequestDto, BusinessHoursEntity::class.java)
  }
}
