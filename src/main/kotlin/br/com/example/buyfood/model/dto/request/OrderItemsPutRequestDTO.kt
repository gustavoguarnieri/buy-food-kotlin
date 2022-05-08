package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class OrderItemsPutRequestDTO(
  val id: @NotNull @Min(1) Long,
  val lineCode: Int = 0,
  val quantity: @NotNull @Min(1) Int,
  val productId: @NotNull @Min(1) Long,
  val status: Int = 1
)
