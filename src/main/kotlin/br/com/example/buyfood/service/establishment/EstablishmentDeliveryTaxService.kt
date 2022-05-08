package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.BadRequestException
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.EstablishmentDeliveryTaxPutRequestDTO
import br.com.example.buyfood.model.dto.request.EstablishmentDeliveryTaxRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentDeliveryTaxResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentDeliveryTaxEntity
import br.com.example.buyfood.model.repository.EstablishmentDeliveryTaxRepository
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class EstablishmentDeliveryTaxService(
  private val modelMapper: ModelMapper,
  private val establishmentDeliveryTaxRepository: EstablishmentDeliveryTaxRepository,
  private val userService: UserService,
  private val statusValidation: StatusValidation
) {

  fun getDeliveryTaxList(status: Int?): List<EstablishmentDeliveryTaxResponseDTO>? {
    return if (status == null) {
      establishmentDeliveryTaxRepository.findAll().map { convertToDto(it) }
    } else {
      establishmentDeliveryTaxRepository
        .findAllByStatus(statusValidation.getStatusIdentification(status))
        ?.map { convertToDto(it) }
    }
  }

  fun getDeliveryTax(deliveryTaxId: Long): EstablishmentDeliveryTaxResponseDTO {
    val establishmentDeliveryTaxResponseDTO = establishmentDeliveryTaxRepository
      .findById(deliveryTaxId)
      .map { convertToDto(it) }

    return if (establishmentDeliveryTaxResponseDTO.isPresent) {
      establishmentDeliveryTaxResponseDTO.get()
    } else {
      throw NotFoundException(ErrorMessages.DELIVERY_TAX_NOT_FOUND)
    }
  }

  fun getMyDeliveryTaxList(status: Int?): List<EstablishmentDeliveryTaxResponseDTO>? {
    return if (status == null) {
      establishmentDeliveryTaxRepository
        .findAllByAuditCreatedBy(userService.userId())
        ?.map { convertToDto(it) }
    } else {
      when (status) {
        1 -> getMyDeliveryTaxListByAuditCreatedByAndStatus(RegisterStatus.ENABLED)
        0 -> {
          getMyDeliveryTaxListByAuditCreatedByAndStatus(RegisterStatus.DISABLED)
        }
        else -> throw BadRequestException("Status incompatible")
      }
    }
  }

  fun createDeliveryTax(
    establishmentDeliveryTaxRequestDto: EstablishmentDeliveryTaxRequestDTO
  ): EstablishmentDeliveryTaxResponseDTO {
    val convertedDeliveryTaxEntity = convertToEntity(establishmentDeliveryTaxRequestDto)
    return convertToDto(establishmentDeliveryTaxRepository.save(convertedDeliveryTaxEntity))
  }

  fun updateDeliveryTax(
    deliveryTaxId: Long?,
    establishmentDeliveryTaxPutRequestDto: EstablishmentDeliveryTaxPutRequestDTO
  ) {
    val convertedDeliveryTaxEntity = convertToEntity(establishmentDeliveryTaxPutRequestDto)
    convertedDeliveryTaxEntity.apply { id = deliveryTaxId }
    establishmentDeliveryTaxRepository.save(convertedDeliveryTaxEntity)
  }

  fun deleteDeliveryTax(deliveryTaxId: Long) {
    val deliveryTaxEntity = getDeliveryTaxById(deliveryTaxId)
    deliveryTaxEntity.apply { status = RegisterStatus.DISABLED.value }
    establishmentDeliveryTaxRepository.save(deliveryTaxEntity)
  }

  fun getDeliveryTaxById(deliveryTaxId: Long): EstablishmentDeliveryTaxEntity {
    val establishmentDeliveryTaxEntity = establishmentDeliveryTaxRepository.findById(deliveryTaxId)

    return if (establishmentDeliveryTaxEntity.isPresent) {
      establishmentDeliveryTaxEntity.get()
    } else {
      throw NotFoundException(ErrorMessages.DELIVERY_TAX_NOT_FOUND)
    }
  }

  private fun getMyDeliveryTaxListByAuditCreatedByAndStatus(
    enabled: RegisterStatus
  ): List<EstablishmentDeliveryTaxResponseDTO>? {
    return establishmentDeliveryTaxRepository
      .findAllByAuditCreatedByAndStatus(userService.userId(), enabled.value)
      ?.map { convertToDto(it) }
  }

  private fun convertToDto(
    establishmentDeliveryTaxEntity: EstablishmentDeliveryTaxEntity?
  ): EstablishmentDeliveryTaxResponseDTO {
    return modelMapper.map(establishmentDeliveryTaxEntity, EstablishmentDeliveryTaxResponseDTO::class.java)
  }

  private fun convertToEntity(deliveryTaxRequestDto: Any): EstablishmentDeliveryTaxEntity {
    return modelMapper.map(deliveryTaxRequestDto, EstablishmentDeliveryTaxEntity::class.java)
  }
}
