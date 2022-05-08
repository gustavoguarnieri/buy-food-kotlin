package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.NotBlank

class EstablishmentCategoryRequestDTO {
  val id: Long? = null
  val description: @NotBlank String? = null
  val status = 1
}
