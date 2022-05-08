package br.com.example.buyfood.model.embeddable

import br.com.example.buyfood.constant.User.UNKNOWN_USER
import br.com.example.buyfood.service.KeycloakService
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@Embeddable
class AuditEmbeddable : Serializable {

  @Column(updatable = false)
  var createdBy: String? = null

  @Column(updatable = false)
  var creationDate: LocalDateTime? = null

  var lastUpdatedBy: String? = null

  var lastUpdatedDate: LocalDateTime? = null

  @PrePersist
  fun prePersist() {
    creationDate = LocalDateTime.now()
    createdBy = KeycloakService().getKeycloakClaims()?.get("user_id").toString().ifBlank { UNKNOWN_USER }
  }

  @PreUpdate
  fun preUpdate() {
    lastUpdatedDate = LocalDateTime.now()
    lastUpdatedBy = KeycloakService().getKeycloakClaims()?.get("user_id").toString().ifBlank { UNKNOWN_USER }
  }
}
