package br.com.example.buyfood.model.dto.request

import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ProductRequestDTO {
  val name: @NotBlank String? = null
  val price: @NotNull BigDecimal? = null
  val description: String? = null
  val status = 1
}
