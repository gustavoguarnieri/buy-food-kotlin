package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class EstablishmentResponseForBusinessHoursDTO {
  var id: Long? = null
  var companyName: String? = null
  var tradingName: String? = null
  var status = 0
}
