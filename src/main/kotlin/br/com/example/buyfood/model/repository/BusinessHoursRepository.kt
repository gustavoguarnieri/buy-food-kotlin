package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.BusinessHoursEntity
import br.com.example.buyfood.model.entity.EstablishmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessHoursRepository : JpaRepository<BusinessHoursEntity?, Long?> {
  fun findAllByEstablishment(establishment: EstablishmentEntity?): List<BusinessHoursEntity?>?
  fun findAllByEstablishmentAndStatus(establishment: EstablishmentEntity?, status: Int): List<BusinessHoursEntity?>?
  fun findByEstablishmentAndId(establishment: EstablishmentEntity?, businessHoursId: Long?): BusinessHoursEntity?
  fun findByEstablishment(establishment: EstablishmentEntity?): BusinessHoursEntity?
  fun findAllByAuditCreatedBy(userId: String?): List<BusinessHoursEntity?>?
  fun findAllByAuditCreatedByAndStatus(userId: String?, status: Int): List<BusinessHoursEntity?>?
}
