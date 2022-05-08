package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class UserSigninRequestDTO {
  val email: @NotBlank @Email String? = null
  val password: @NotBlank String? = null
}
