package br.com.example.buyfood.model.dto.request

import javax.validation.constraints.NotBlank

class UserUpdateRequestDTO {
  val email: String? = null
  val firstName: @NotBlank String? = null
  val lastName: String? = null
  val nickName: String? = null
  val phone: String? = null
  val password: String? = null
  val role: String? = null
}
