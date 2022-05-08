package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.EstablishmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EstablishmentRepository : JpaRepository<EstablishmentEntity?, Long?> {
  fun findAllByStatus(status: Int): List<EstablishmentEntity?>?
  fun findAllByAuditCreatedBy(userId: String?): List<EstablishmentEntity?>?
  fun findAllByAuditCreatedByAndStatus(userId: String?, status: Int): List<EstablishmentEntity?>?
}
