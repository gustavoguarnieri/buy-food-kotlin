package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.NotBlank

class IngredientRequestDTO {
  val ingredient: @NotBlank String? = null
  val portion: String? = null
  val status = 1
}
