package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class UserResponseDTO {
  var email: String? = null
  var firstName: String? = null
  var lastName: String? = null
  var nickName: String? = null
  var birthDate: String? = null
  var phone: String? = null
  var cpf: String? = null
  var cnpj: String? = null
  var status = 0
}
