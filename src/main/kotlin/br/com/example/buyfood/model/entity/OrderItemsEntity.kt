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
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "order_items")
class OrderItemsEntity : Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  var id: Long? = null

  var lineCode = 0

  @Column(nullable = false)
  var quantity: @NotNull Int = 0

  @Column(nullable = false)
  var price: @NotNull BigDecimal = BigDecimal.ZERO

  @ManyToOne
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  var order: OrderEntity? = null

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  var product: ProductEntity? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
