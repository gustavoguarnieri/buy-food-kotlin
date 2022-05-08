package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OrderPutRequestDTO(
  val establishmentId: @NotNull Long,
  val deliveryAddressId: @NotNull Long,
  val items: List<OrderItemsPutRequestDTO>? = null,
  val paymentWayId: @NotNull @Min(1) Long,
  val paymentStatus: @NotBlank String,
  val preparationStatus: PreparationStatusRequestDTO? = null,
  val observation: String? = null,
  val status: Int = 1
)
