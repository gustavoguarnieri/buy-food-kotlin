package br.com.example.buyfood.model.dto.response.product

import br.com.example.buyfood.model.dto.response.IngredientResponseDTO
import br.com.example.buyfood.model.dto.response.UploadFileResponseDTO
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class EstablishmentProductResponseDTO {
  var id: Long? = null
  var name: String? = null
  var price: String? = null
  var description: String? = null
  var images: MutableList<UploadFileResponseDTO>? = null
  var establishment: EstablishmentResponseForProductDTO? = null
  var ingredients: MutableList<IngredientResponseDTO>? = null
  var status = 0
}
