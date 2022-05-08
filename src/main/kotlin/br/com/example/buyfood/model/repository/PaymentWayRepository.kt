package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.PaymentWayEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentWayRepository : JpaRepository<PaymentWayEntity?, Long?> {
  fun findAllByStatus(status: Int): List<PaymentWayEntity?>?
}
