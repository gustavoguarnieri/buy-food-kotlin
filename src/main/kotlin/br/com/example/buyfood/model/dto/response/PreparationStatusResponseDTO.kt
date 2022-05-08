package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class PreparationStatusResponseDTO {
  var id: Long? = null
  var description: String? = null
  var status = 0
}
