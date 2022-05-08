package br.com.example.buyfood.model.dto.response

import br.com.example.buyfood.enums.EstablishmentCategory
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class CategoryResponseDTO {
  var id: Long? = null
  var companyName: String? = null
  var tradingName: String? = null
  var email: String? = null
  var commercialPhone: String? = null
  var mobilePhone: String? = null
  var category: EstablishmentCategory? = null
}
