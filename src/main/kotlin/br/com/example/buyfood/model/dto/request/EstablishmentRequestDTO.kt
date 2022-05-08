package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class EstablishmentRequestDTO {
  val companyName: @NotBlank String? = null
  val tradingName: @NotBlank String? = null
  val email: @NotBlank @Email String? = null
  val commercialPhone: String? = null
  val mobilePhone: String? = null
  val category: EstablishmentCategoryRequestDTO? = null
  val deliveryTax: EstablishmentDeliveryTaxRequestDTO? = null
  val status = 1
}
