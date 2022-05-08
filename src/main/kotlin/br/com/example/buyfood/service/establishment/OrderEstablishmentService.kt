package br.com.example.buyfood.service.establishment

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.response.OrderResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.repository.EstablishmentRepository
import br.com.example.buyfood.model.repository.OrderItemsRepository
import br.com.example.buyfood.model.repository.OrderRepository
import br.com.example.buyfood.service.OrderService
import br.com.example.buyfood.service.UserService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class OrderEstablishmentService(
  override val modelMapper: ModelMapper,
  override val productEstablishmentService: ProductEstablishmentService,
  override val preparationStatusService: EstablishmentPreparationStatusService,
  override val paymentWayService: EstablishmentPaymentWayService,
  override val orderItemsRepository: OrderItemsRepository,
  override val orderRepository: OrderRepository,
  val establishmentRepository: EstablishmentRepository,
  override val statusValidation: StatusValidation,
  override val userService: UserService
) : OrderService(
  modelMapper,
  productEstablishmentService,
  preparationStatusService,
  paymentWayService,
  orderItemsRepository,
  orderRepository,
  statusValidation,
  userService
) {

  fun getOrderList(status: Int?, establishment: Int?): List<OrderResponseDTO?>? {
    return status?.let { getEstablishmentOrderListByStatus(establishment, it) } ?: getEstablishmentOrderList(
      establishment
    )
  }

  private fun getEstablishmentOrderList(establishmentId: Int?): List<OrderResponseDTO?> {
    return establishmentId?.let { id ->
      val establishmentEntity = getEstablishmentById(id)
      orderRepository.findAllByEstablishment(establishmentEntity)?.map { convertToDto(it) }
    } ?: run {
      orderRepository.findAll().map { convertToDto(it) }
    }
  }

  private fun getEstablishmentOrderListByStatus(establishmentId: Int?, status: Int): List<OrderResponseDTO>? {
    val statusIdentification = statusValidation.getStatusIdentification(status)

    return establishmentId?.let { id ->
      val establishmentEntity = getEstablishmentById(id)
      orderRepository.findAllByEstablishmentAndStatus(establishmentEntity, statusIdentification)
        ?.map { convertToDto(it) }
    } ?: run {
      orderRepository
        .findAllByStatus(statusValidation.getStatusIdentification(statusIdentification))
        ?.map { convertToDto(it) }
    }
  }

  private fun getEstablishmentById(establishmentId: Int): EstablishmentEntity {
    val establishmentRepository = establishmentRepository.findById(establishmentId.toLong())

    return if (establishmentRepository.isPresent) {
      establishmentRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.ESTABLISHMENT_ORDER_NOT_FOUND)
    }
  }
}
