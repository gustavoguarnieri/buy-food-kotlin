package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.NotNull

class OrderItemsRequestDTO {
  val id: @NotNull Long? = null
  val lineCode = 0
  val quantity: @NotNull Int? = null
  val productId: @NotNull Long? = null
}
