package br.com.example.buyfood.model.dto.request

import br.com.example.buyfood.enums.State
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class DeliveryAddressRequestDTO {
  val recipientName: String? = null
  val zipCode: @NotBlank String? = null
  val address: @NotBlank String? = null
  val addressNumber: @NotNull Int? = null
  val neighbourhood: @NotBlank String? = null
  val city: @NotBlank String? = null
  val state: State? = null
  val observation: String? = null
  val status = 1
}
