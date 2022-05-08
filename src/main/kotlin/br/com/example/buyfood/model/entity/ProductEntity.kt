package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "product")
class ProductEntity : Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  var images: MutableList<ImageEntity>? = null

  @ManyToOne
  @JoinColumn(name = "establishment_id", referencedColumnName = "id")
  var establishment: EstablishmentEntity? = null

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  var ingredients: MutableList<IngredientEntity>? = null

  @Column(nullable = false, length = 50)
  var name: @NotBlank String? = null

  @Column(nullable = false)
  var price: @NotNull BigDecimal = BigDecimal.ZERO

  @Column(nullable = false)
  var description: @NotNull String? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
