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
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "image")
class ImageEntity : Serializable {

  constructor(
    product: ProductEntity?,
    fileName: @NotBlank String?,
    fileUri: @NotNull String?,
    fileType: String?,
    size: Long
  ) {
    this.product = product
    this.fileName = fileName
    this.fileUri = fileUri
    this.fileType = fileType
    this.size = size
  }

  constructor(
    establishment: EstablishmentEntity?,
    fileName: @NotBlank String?,
    fileUri: @NotNull String?,
    fileType: String?,
    size: Long
  ) {
    this.establishment = establishment
    this.fileName = fileName
    this.fileUri = fileUri
    this.fileType = fileType
    this.size = size
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @ManyToOne
  @JoinColumn(name = "product_id")
  var product: ProductEntity? = null

  @ManyToOne
  @JoinColumn(name = "establishment_id")
  var establishment: EstablishmentEntity? = null

  @Column(nullable = false, length = 150)
  var fileName: @NotBlank String? = null

  @Column(nullable = false)
  var fileUri: @NotNull String? = null

  private var fileType: String? = null

  var size: Long = 0

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
