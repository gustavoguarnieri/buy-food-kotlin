package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "establishment_category")
class EstablishmentCategoryEntity : Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(nullable = false)
  var description: @NotBlank String? = null

  @Column(nullable = false)
  var status: Int = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
