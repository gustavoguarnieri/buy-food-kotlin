package br.com.example.buyfood.util

import br.com.example.buyfood.enums.RegisterStatus
import org.springframework.stereotype.Component

@Component
class StatusValidation {
  fun getStatusIdentification(status: Int?): Int {
    return if (status == 0) RegisterStatus.DISABLED.value else RegisterStatus.ENABLED.value
  }
}
