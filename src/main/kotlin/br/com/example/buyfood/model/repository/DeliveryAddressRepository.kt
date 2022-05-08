package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.DeliveryAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeliveryAddressRepository : JpaRepository<DeliveryAddressEntity?, Long?> {
  fun findAllByStatus(status: Int): List<DeliveryAddressEntity?>?
  fun findAllByAuditCreatedByAndStatus(userId: String?, status: Int): List<DeliveryAddressEntity?>?
}
