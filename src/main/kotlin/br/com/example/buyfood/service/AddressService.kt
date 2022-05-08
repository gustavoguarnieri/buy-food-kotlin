package br.com.example.buyfood.service

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.DeliveryAddressRequestDTO
import br.com.example.buyfood.model.dto.response.DeliveryAddressResponseDTO
import br.com.example.buyfood.model.entity.DeliveryAddressEntity
import br.com.example.buyfood.model.repository.DeliveryAddressRepository
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class AddressService(
  private val modelMapper: ModelMapper,
  private val deliveryAddressRepository: DeliveryAddressRepository,
  private val statusValidation: StatusValidation,
  private val userService: UserService
) {

  fun getUserAddressList(status: Int?): List<DeliveryAddressResponseDTO>? {
    return deliveryAddressRepository.findAllByStatus(statusValidation.getStatusIdentification(status))
      ?.map { convertToDto(it) }
  }

  fun getAddressListByCreatedBy(status: Int?): List<DeliveryAddressResponseDTO>? {
    return deliveryAddressRepository
      .findAllByAuditCreatedByAndStatus(userService.userId(), statusValidation.getStatusIdentification(status))
      ?.map { convertToDto(it) }
  }

  fun getUserAddress(addressId: Long): DeliveryAddressResponseDTO {
    return convertToDto(getUserAddressById(addressId))
  }

  fun createUserAddress(
    deliveryAddressRequestDto: DeliveryAddressRequestDTO
  ): DeliveryAddressResponseDTO {
    val deliveryAddressEntity = convertToEntity(deliveryAddressRequestDto)
    return convertToDto(deliveryAddressRepository.save(deliveryAddressEntity))
  }

  fun updateUserAddress(
    addressId: Long,
    deliveryAddressRequestDto: DeliveryAddressRequestDTO
  ) {
    getUserAddressById(addressId)
    val deliveryAddressEntity = convertToEntity(deliveryAddressRequestDto)
    deliveryAddressEntity.apply { id = addressId }
    deliveryAddressRepository.save(deliveryAddressEntity)
  }

  fun deleteUserAddress(addressId: Long) {
    val userAddress = getUserAddressById(addressId)
    userAddress.apply { status = RegisterStatus.DISABLED.value }
    deliveryAddressRepository.save(userAddress)
  }

  fun getUserAddressById(addressId: Long): DeliveryAddressEntity {
    val deliveryAddressRepository = deliveryAddressRepository.findById(addressId)

    return if (deliveryAddressRepository.isPresent) {
      deliveryAddressRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.ADDRESS_NOT_FOUND)
    }
  }

  private fun convertToDto(deliveryAddressEntity: DeliveryAddressEntity?): DeliveryAddressResponseDTO {
    return modelMapper.map(deliveryAddressEntity, DeliveryAddressResponseDTO::class.java)
  }

  private fun convertToEntity(
    deliveryAddressRequestDto: DeliveryAddressRequestDTO
  ): DeliveryAddressEntity {
    return modelMapper.map(deliveryAddressRequestDto, DeliveryAddressEntity::class.java)
  }
}
