package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
class ProductResponseDTO {
  var id: Long? = null
  var name: String? = null
  var price: BigDecimal? = null
  var description: String? = null
  var status = 0
}
