package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_keycloak")
class UserEntity : Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
  var userId: String? = null

  @Column(nullable = false)
  var firstName: String? = null

  @Column(nullable = false)
  var lastName: String? = null
  var nickName: String? = null

  @Column(nullable = false)
  var email: String? = null
  var phone: String? = null
  var birthDate: LocalDate? = null
  var cpf: @CPF String? = null
  var cnpj: @CNPJ String? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
