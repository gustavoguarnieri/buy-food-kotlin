package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class DeliveryAddressResponseDTO {
  var id: Long? = null
  var recipientName: String? = null
  var zipCode: String? = null
  var address: String? = null
  var addressNumber: Int? = null
  var neighbourhood: String? = null
  var city: String? = null
  var state: String? = null
  var observation: String? = null
  var status = 0
}
