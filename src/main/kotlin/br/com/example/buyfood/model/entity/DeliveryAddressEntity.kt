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
import javax.validation.constraints.NotNull

@Entity
@Table(name = "delivery_address")
class DeliveryAddressEntity : Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
  var recipientName: String? = null

  @Column(nullable = false)
  var zipCode: @NotBlank String? = null

  @Column(nullable = false)
  var address: @NotBlank String? = null

  @Column(nullable = false)
  var addressNumber: @NotNull Int? = null

  @Column(nullable = false)
  var neighbourhood: @NotBlank String? = null

  @Column(nullable = false)
  var city: @NotBlank String? = null

  @Column(nullable = false)
  var state: @NotBlank String? = null
  var observation: String? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
