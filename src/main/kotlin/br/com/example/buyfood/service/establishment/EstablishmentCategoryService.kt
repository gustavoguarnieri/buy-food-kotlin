package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.EstablishmentCategoryRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentCategoryResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentCategoryEntity
import br.com.example.buyfood.model.repository.EstablishmentCategoryRepository
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class EstablishmentCategoryService(
  private val modelMapper: ModelMapper,
  private val establishmentCategoryRepository: EstablishmentCategoryRepository,
  private val statusValidation: StatusValidation
) {

  fun getEstablishmentCategoryList(status: Int?): List<EstablishmentCategoryResponseDTO>? {
    return if (status == null) {
      establishmentCategoryRepository.findAll().map { convertToDto(it) }
    } else {
      establishmentCategoryRepository
        .findAllByStatus(statusValidation.getStatusIdentification(status))
        ?.map { convertToDto(it) }
    }
  }

  fun getEstablishmentCategory(id: Long): EstablishmentCategoryResponseDTO {
    val establishmentCategoryResponseDTO = establishmentCategoryRepository.findById(id).map { convertToDto(it) }
    return if (establishmentCategoryResponseDTO.isPresent) {
      establishmentCategoryResponseDTO.get()
    } else {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_NOT_FOUND)
    }
  }

  fun createEstablishmentCategory(
    establishmentCategoryRequestDto: EstablishmentCategoryRequestDTO
  ): EstablishmentCategoryResponseDTO {
    val convertedEstablishmentCategoryEntity = convertToEntity(establishmentCategoryRequestDto)
    return convertToDto(establishmentCategoryRepository.save(convertedEstablishmentCategoryEntity))
  }

  fun updateEstablishmentCategory(establishmentId: Long?, establishmentRequestDto: EstablishmentCategoryRequestDTO) {
    val convertedEstablishmentEntity = convertToEntity(establishmentRequestDto)
    convertedEstablishmentEntity.apply { id = establishmentId }
    establishmentCategoryRepository.save(convertedEstablishmentEntity)
  }

  fun deleteEstablishmentCategory(id: Long) {
    val establishmentEntity = getEstablishmentCategoryById(id)
    establishmentEntity.apply { status = RegisterStatus.DISABLED.value }
    establishmentCategoryRepository.save(establishmentEntity)
  }

  fun getEstablishmentCategoryById(id: Long): EstablishmentCategoryEntity {
    val establishmentCategoryEntity = establishmentCategoryRepository.findById(id)

    return if (establishmentCategoryEntity.isPresent) {
      establishmentCategoryEntity.get()
    } else {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_CATEGORY_NOT_FOUND)
    }
  }

  private fun convertToDto(
    establishmentCategoryEntity: EstablishmentCategoryEntity?
  ): EstablishmentCategoryResponseDTO {
    return modelMapper.map(establishmentCategoryEntity, EstablishmentCategoryResponseDTO::class.java)
  }

  private fun convertToEntity(
    establishmentCategoryRequestDto: EstablishmentCategoryRequestDTO
  ): EstablishmentCategoryEntity {
    return modelMapper.map(establishmentCategoryRequestDto, EstablishmentCategoryEntity::class.java)
  }
}
