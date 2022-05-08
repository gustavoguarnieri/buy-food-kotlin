package br.com.example.buyfood.model.dto.response.product

import br.com.example.buyfood.model.dto.response.EstablishmentCategoryResponseDTO
import br.com.example.buyfood.model.dto.response.EstablishmentDeliveryTaxResponseDTO
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class EstablishmentResponseForProductDTO {
  var id: Long? = null
  var companyName: String? = null
  var tradingName: String? = null
  var email: String? = null
  var commercialPhone: String? = null
  var mobilePhone: String? = null
  var category: EstablishmentCategoryResponseDTO? = null
  var businessHours: BusinessHoursResponseDTO? = null
  var deliveryTax: EstablishmentDeliveryTaxResponseDTO? = null
  var status = 0
}
