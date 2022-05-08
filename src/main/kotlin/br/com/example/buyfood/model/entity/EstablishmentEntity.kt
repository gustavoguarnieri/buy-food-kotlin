package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "establishment")
class EstablishmentEntity : Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @OneToMany(mappedBy = "establishment", fetch = FetchType.LAZY)
  var images: MutableList<ImageEntity>? = null

  @OneToOne(mappedBy = "establishment", fetch = FetchType.LAZY)
  var businessHours: BusinessHoursEntity? = null

  @OneToMany(mappedBy = "establishment", fetch = FetchType.LAZY)
  var product: MutableList<ProductEntity>? = null

  @ManyToOne
  var category: EstablishmentCategoryEntity? = null

  @ManyToOne
  var delivery: EstablishmentDeliveryTaxEntity? = null

  @Column(nullable = false)
  var companyName: @NotBlank String? = null

  @Column(nullable = false)
  var tradingName: @NotBlank String? = null

  @Column(nullable = false)
  var email: @NotBlank String? = null
  var commercialPhone: String? = null
  var mobilePhone: String? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
