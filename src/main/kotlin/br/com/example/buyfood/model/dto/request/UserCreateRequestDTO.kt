package br.com.example.buyfood.model.dto.request

import br.com.example.buyfood.enums.Role
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class UserCreateRequestDTO(
  val cpf: @CPF String,
  val cnpj: @CNPJ String,
  val firstName: @NotBlank String,
  val lastName: @NotBlank String,
  val nickName: String,
  val phone: @NotBlank String,
  val birthDate: LocalDate? = null,
  val email: @NotBlank @Email String,
  val password: @NotBlank String,
  val role: @NotBlank Role? = null
)
