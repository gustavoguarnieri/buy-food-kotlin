package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.NotBlank

class PaymentWayRequestDTO {
  val id: Long? = null
  val description: @NotBlank String? = null
  val status = 1
}
