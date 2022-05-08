package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "establishment_delivery_tax")
class EstablishmentDeliveryTaxEntity : Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(nullable = false)
  var taxAmount: BigDecimal? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
