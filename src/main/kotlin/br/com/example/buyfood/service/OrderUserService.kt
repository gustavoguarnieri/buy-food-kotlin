package br.com.example.buyfood.service

import br.com.example.buyfood.enums.PreparationStatusDefault
import br.com.example.buyfood.exception.NotFoundException
import br.com.example.buyfood.model.dto.request.OrderRequestDTO
import br.com.example.buyfood.model.dto.response.OrderResponseDTO
import br.com.example.buyfood.model.repository.OrderItemsRepository
import br.com.example.buyfood.model.repository.OrderRepository
import br.com.example.buyfood.model.repository.PreparationStatusRepository
import br.com.example.buyfood.service.establishment.EstablishmentPaymentWayService
import br.com.example.buyfood.service.establishment.EstablishmentPreparationStatusService
import br.com.example.buyfood.service.establishment.ProductEstablishmentService
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderUserService(
  override val modelMapper: ModelMapper,
  override val productEstablishmentService: ProductEstablishmentService,
  override val preparationStatusService: EstablishmentPreparationStatusService,
  override val paymentWayService: EstablishmentPaymentWayService,
  override val orderItemsRepository: OrderItemsRepository,
  override val orderRepository: OrderRepository,
  override val statusValidation: StatusValidation,
  override val userService: UserService,
  val addressService: AddressService,
  val preparationStatusRepository: PreparationStatusRepository,
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

  fun createOrder(orderRequestDto: OrderRequestDTO): OrderResponseDTO {
    val convertedOrderEntity = convertToEntity(orderRequestDto)
    convertedOrderEntity.id = null
    val deliveryAddress = addressService.getUserAddressById(orderRequestDto.deliveryAddressId)
    convertedOrderEntity.deliveryAddress = deliveryAddress
    val preparationStatusId =
      orderRequestDto.preparationStatus?.id ?: run {
        preparationStatusRepository.findByDescriptionIgnoreCase(PreparationStatusDefault.PENDENTE.name)?.id
          ?: throw NotFoundException(PREPARATION_STATUS_PENDING_NOT_FOUND)
      }
    val preparationStatus = preparationStatusService.getPreparationStatusById(preparationStatusId)
    convertedOrderEntity.preparationStatus = preparationStatus
    val paymentWay = paymentWayService.getPaymentWayById(orderRequestDto.paymentWayId)
    convertedOrderEntity.paymentWay = paymentWay
    var count = 1
    convertedOrderEntity.items?.forEach { i ->
      i.id = null
      i.lineCode = count++
      i.product?.price =
        i.product?.id?.let {
        productEstablishmentService
          .getProduct(orderRequestDto.establishmentId, it)
          .price
      } ?: BigDecimal.ZERO
      i.order = convertedOrderEntity
    }
    val orderEntity = orderRepository.save(convertedOrderEntity)
    return convertToDto(orderEntity)
  }

  companion object {
    private const val PREPARATION_STATUS_PENDING_NOT_FOUND = "Não existe o status de preparo padrão: (Pendente)"
  }
}
