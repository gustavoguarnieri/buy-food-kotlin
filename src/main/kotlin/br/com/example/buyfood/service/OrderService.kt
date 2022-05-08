package br.com.example.buyfood.service

import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.OrderItemsPutRequestDTO
import br.com.example.buyfood.model.dto.request.OrderPutRequestDTO
import br.com.example.buyfood.model.dto.request.OrderRequestDTO
import br.com.example.buyfood.model.dto.response.OrderResponseDTO
import br.com.example.buyfood.model.entity.OrderEntity
import br.com.example.buyfood.model.entity.OrderItemsEntity
import br.com.example.buyfood.model.repository.OrderItemsRepository
import br.com.example.buyfood.model.repository.OrderRepository
import br.com.example.buyfood.service.establishment.EstablishmentPaymentWayService
import br.com.example.buyfood.service.establishment.EstablishmentPreparationStatusService
import br.com.example.buyfood.service.establishment.ProductEstablishmentService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderService(
  val modelMapper: ModelMapper,
  val productEstablishmentService: ProductEstablishmentService,
  val preparationStatusService: EstablishmentPreparationStatusService,
  val paymentWayService: EstablishmentPaymentWayService,
  val orderItemsRepository: OrderItemsRepository,
  val orderRepository: OrderRepository,
  val statusValidation: StatusValidation,
  val userService: UserService
) {
  fun updateOrder(orderId: Long, orderPutRequestDto: OrderPutRequestDTO) {
    val orderEntity = getOrderById(orderId)
    orderEntity.paymentStatus = orderPutRequestDto.paymentStatus
    val paymentWay = paymentWayService.getPaymentWayById(orderPutRequestDto.paymentWayId)
    orderEntity.paymentWay = paymentWay
    val preparationStatus = orderPutRequestDto.preparationStatus?.id?.let {
      preparationStatusService.getPreparationStatusById(it)
    }
    orderEntity.preparationStatus = preparationStatus
    orderEntity.status = orderPutRequestDto.status
    orderRepository.save(orderEntity)
    orderPutRequestDto
      .items
      ?.forEach { i ->
        val convertedOrderItemEntity = convertToEntity(i)
        convertedOrderItemEntity.order = orderEntity
        convertedOrderItemEntity.price = productEstablishmentService.getProduct(
          orderPutRequestDto.establishmentId, i.productId
        ).price ?: BigDecimal.ZERO
        convertedOrderItemEntity.status = i.status
        orderItemsRepository.save(convertedOrderItemEntity)
      }
  }

  fun deleteOrder(orderId: Long) {
    val orderEntity = getOrderById(orderId)
    orderEntity.status = RegisterStatus.DISABLED.value
    orderRepository.save(orderEntity)
  }

  private fun getOrderById(orderId: Long): OrderEntity {
    val orderRepository = orderRepository.findById(orderId)

    return if (orderRepository.isPresent) {
      orderRepository.get()
    } else {
      throw NotFoundException(ErrorMessages.ORDER_NOT_FOUND)
    }
  }

  fun getOrder(orderId: Long): OrderResponseDTO {
    val orderRepository = orderRepository.findById(orderId)

    if (!orderRepository.isPresent) {
      throw NotFoundException(ErrorMessages.ORDER_NOT_FOUND)
    }

    return convertToDto(orderRepository.get())
  }

  fun getOrderList(status: Int?): List<OrderResponseDTO>? {
    return orderRepository
      .findAllByStatus(statusValidation.getStatusIdentification(status))
      ?.map { convertToDto(it) }
  }

  fun getOrderListByCreatedBy(status: Int?): List<OrderResponseDTO>? {
    return orderRepository
      .findAllByAuditCreatedByAndStatus(
        userService.userId(), statusValidation.getStatusIdentification(status)
      )
      ?.map { convertToDto(it) }
  }

  protected fun convertToDto(orderEntity: OrderEntity?): OrderResponseDTO {
    return modelMapper.map(orderEntity, OrderResponseDTO::class.java)
  }

  protected fun convertToEntity(orderRequestDto: OrderRequestDTO?): OrderEntity {
    return modelMapper.map(orderRequestDto, OrderEntity::class.java)
  }

  protected fun convertToEntity(orderItemsPutRequestDto: OrderItemsPutRequestDTO?): OrderItemsEntity {
    return modelMapper.map(orderItemsPutRequestDto, OrderItemsEntity::class.java)
  }
}
