package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.PreparationStatusRequestDTO
import br.com.example.buyfood.model.dto.response.PreparationStatusResponseDTO
import br.com.example.buyfood.model.entity.PreparationStatusEntity
import br.com.example.buyfood.model.repository.PreparationStatusRepository
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class EstablishmentPreparationStatusService(
  private val modelMapper: ModelMapper,
  private val preparationStatusRepository: PreparationStatusRepository,
  private val statusValidation: StatusValidation
) {

  fun getPreparationStatusList(status: Int?): List<PreparationStatusResponseDTO>? {
    return if (status == null) {
      preparationStatusRepository.findAll()
        .mapNotNull { convertToDto(it) }
    } else {
      preparationStatusRepository
        .findAllByStatus(statusValidation.getStatusIdentification(status))
        ?.map { convertToDto(it) }
    }
  }

  fun getPreparationStatus(id: Long): PreparationStatusResponseDTO {
    val preparationStatusRepository = preparationStatusRepository
      .findById(id)
      .map { convertToDto(it) }

    return if (preparationStatusRepository.isPresent) {
      preparationStatusRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PREPARATION_STATUS_NOT_FOUND)
    }
  }

  fun createPreparationStatus(
    preparationStatusRequestDTO: PreparationStatusRequestDTO
  ): PreparationStatusResponseDTO {
    val convertedPreparationStatusEntity = convertToEntity(preparationStatusRequestDTO)
    return convertToDto(preparationStatusRepository.save(convertedPreparationStatusEntity))
  }

  fun updatePreparationStatus(preparationStatusId: Long?, preparationStatusRequestDTO: PreparationStatusRequestDTO) {
    val convertedPreparationStatusEntity = convertToEntity(preparationStatusRequestDTO)
    convertedPreparationStatusEntity.apply { id = preparationStatusId }
    preparationStatusRepository.save(convertedPreparationStatusEntity)
  }

  fun deletePreparationStatus(id: Long) {
    val convertedPreparationStatusEntity = getPreparationStatusById(id)
    convertedPreparationStatusEntity.apply { status = RegisterStatus.DISABLED.value }
    preparationStatusRepository.save(convertedPreparationStatusEntity)
  }

  fun getPreparationStatusById(id: Long): PreparationStatusEntity {
    val preparationStatusRepository = preparationStatusRepository.findById(id)

    return if (preparationStatusRepository.isPresent) {
      preparationStatusRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PREPARATION_STATUS_NOT_FOUND)
    }
  }

  private fun convertToDto(preparationStatusEntity: PreparationStatusEntity?): PreparationStatusResponseDTO {
    return modelMapper.map(preparationStatusEntity, PreparationStatusResponseDTO::class.java)
  }

  private fun convertToEntity(preparationStatusRequestDTO: PreparationStatusRequestDTO): PreparationStatusEntity {
    return modelMapper.map(preparationStatusRequestDTO, PreparationStatusEntity::class.java)
  }
}
