package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.PaymentWayRequestDTO
import br.com.example.buyfood.model.dto.response.PaymentWayResponseDTO
import br.com.example.buyfood.model.entity.PaymentWayEntity
import br.com.example.buyfood.model.repository.PaymentWayRepository
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class EstablishmentPaymentWayService(
  private val modelMapper: ModelMapper,
  private val paymentWayRepository: PaymentWayRepository,
  private val statusValidation: StatusValidation
) {

  fun getPaymentWayList(status: Int?): List<PaymentWayResponseDTO>? {
    return if (status == null) {
      paymentWayRepository.findAll().mapNotNull { convertToDto(it) }
    } else {
      paymentWayRepository
        .findAllByStatus(statusValidation.getStatusIdentification(status))
        ?.map { convertToDto(it) }
    }
  }

  fun getPaymentWay(id: Long): PaymentWayResponseDTO {
    val paymentWayRepository = paymentWayRepository
      .findById(id)
      .map { convertToDto(it) }

    return if (paymentWayRepository.isPresent) {
      paymentWayRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PAYMENT_WAY_NOT_FOUND)
    }
  }

  fun createPaymentWay(paymentWayRequestDTO: PaymentWayRequestDTO): PaymentWayResponseDTO {
    val convertedPaymentWayEntity = convertToEntity(paymentWayRequestDTO)
    return convertToDto(paymentWayRepository.save(convertedPaymentWayEntity))
  }

  fun updatePaymentWay(paymentWayId: Long, paymentWayRequestDTO: PaymentWayRequestDTO) {
    val convertedPaymentWayEntity = convertToEntity(paymentWayRequestDTO)
    convertedPaymentWayEntity.apply { id = paymentWayId }
    paymentWayRepository.save(convertedPaymentWayEntity)
  }

  fun deletePaymentWay(id: Long) {
    val convertedPaymentWayEntity = getPaymentWayById(id)
    convertedPaymentWayEntity.apply { status = RegisterStatus.DISABLED.value }
    paymentWayRepository.save(convertedPaymentWayEntity)
  }

  fun getPaymentWayById(id: Long): PaymentWayEntity {
    val paymentWayRepository = paymentWayRepository.findById(id)

    return if (paymentWayRepository.isPresent) {
      paymentWayRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.PAYMENT_WAY_NOT_FOUND)
    }
  }

  private fun convertToDto(paymentWayEntity: PaymentWayEntity?): PaymentWayResponseDTO {
    return modelMapper.map(paymentWayEntity, PaymentWayResponseDTO::class.java)
  }

  private fun convertToEntity(paymentWayRequestDTO: PaymentWayRequestDTO): PaymentWayEntity {
    return modelMapper.map(paymentWayRequestDTO, PaymentWayEntity::class.java)
  }
}
