package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
class EstablishmentDeliveryTaxResponseDTO {
  var id: Long? = null
  var taxAmount: BigDecimal? = null
  var status = 0
}
