package br.com.example.buyfood.model.dto.request

import java.math.BigDecimal
import javax.validation.constraints.NotNull

class EstablishmentDeliveryTaxPutRequestDTO {
  val taxAmount: @NotNull BigDecimal? = null
  val status = 1
}
