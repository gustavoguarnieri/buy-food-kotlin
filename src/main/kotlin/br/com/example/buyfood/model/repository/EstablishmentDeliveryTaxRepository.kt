package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.EstablishmentDeliveryTaxEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EstablishmentDeliveryTaxRepository : JpaRepository<EstablishmentDeliveryTaxEntity?, Long?> {
  fun findAllByStatus(status: Int): List<EstablishmentDeliveryTaxEntity?>?
  fun findAllByAuditCreatedBy(userId: String?): List<EstablishmentDeliveryTaxEntity?>?
  fun findAllByAuditCreatedByAndStatus(userId: String?, status: Int): List<EstablishmentDeliveryTaxEntity?>?
}
