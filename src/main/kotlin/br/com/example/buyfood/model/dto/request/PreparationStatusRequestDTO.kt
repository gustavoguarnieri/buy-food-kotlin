package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class PreparationStatusRequestDTO(
  val id: @NotNull @Min(1) Long,
  val description: @NotBlank String? = null,
  val status: Int = 1
)
