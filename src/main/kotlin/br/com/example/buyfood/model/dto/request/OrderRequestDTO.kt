package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class OrderRequestDTO(
  val establishmentId: @NotNull @Min(1) Long,
  val deliveryAddressId: @NotNull @Min(1) Long,
  val items: List<OrderItemsRequestDTO>? = null,
  val paymentWayId: @NotNull @Min(1) Long,
  val preparationStatus: PreparationStatusRequestDTO? = null,
  val observation: String? = null
)
