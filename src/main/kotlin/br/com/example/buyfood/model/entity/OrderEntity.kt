package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.PaymentStatus
import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "orders")
class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  var id: Long? = null

  @ManyToOne
  @JoinColumn(name = "delivery_address_id", referencedColumnName = "id")
  var deliveryAddress: DeliveryAddressEntity? = null

  @ManyToOne
  @JoinColumn(name = "establishment_id", referencedColumnName = "id")
  var establishment: EstablishmentEntity? = null

  @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
  var items: MutableList<OrderItemsEntity>? = null

  @ManyToOne
  var paymentWay: PaymentWayEntity? = null

  @Column(nullable = false)
  var paymentStatus: @NotBlank String? = PaymentStatus.PENDING.name

  @ManyToOne
  var preparationStatus: PreparationStatusEntity? = null
  var observation: String? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
