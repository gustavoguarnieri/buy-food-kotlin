package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.`interface`.DashboardStatisticsInterface
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface OrderRepository : JpaRepository<OrderEntity?, Long?> {
  @Query(
    """
            select function('date_format', o.audit.creationDate, '%m/%y') as indice, count(o) as value
              from OrderEntity o
             where o.audit.creationDate >= :startDate
          group by function('date_format', o.audit.creationDate, '%m/%y')
        """
  )
  fun findOrdersByMonth(startDate: LocalDateTime?): List<DashboardStatisticsInterface?>?

  @Query(
    """
            select function('date_format', o.audit.creationDate, '%m/%y') as indice, sum(oi.price * oi.quantity) as value
              from OrderEntity o, OrderItemsEntity oi
             where o.id = oi.order.id
               and o.audit.creationDate >= :startDate
          group by function('date_format', o.audit.creationDate, '%m/%y')
        """
  )
  fun findBillingByMonth(startDate: LocalDateTime?): List<DashboardStatisticsInterface?>?

  @Query(
    """
            select pse.description as indice, count(o.id) as value
              from OrderEntity o, PreparationStatusEntity pse
             where o.preparationStatus = pse.id
               and o.audit.creationDate >= :startDate
          group by pse.description
        """
  )
  fun findPreparationStatus(startDate: LocalDateTime?): List<DashboardStatisticsInterface?>?

  @Query(
    """
            select pwe.description as indice, count(o.id) as value
              from OrderEntity o, PaymentWayEntity pwe
              where o.paymentWay = pwe.id
                and o.audit.creationDate >= :startDate
           group by pwe.description
        """
  )
  fun findPaymentWay(startDate: LocalDateTime?): List<DashboardStatisticsInterface?>?

  @Query(
    """
            select function('date_format', o.audit.creationDate, '%m/%y') as indice, count(o.id) as value
              from OrderEntity o
             where o.audit.creationDate >= :startDate
               and o.paymentStatus = :paymentStatus
          group by function('date_format', o.audit.creationDate, '%m/%y')
        """
  )
  fun findPaymentDeclinedStatus(
    startDate: LocalDateTime?,
    paymentStatus: String?
  ): List<DashboardStatisticsInterface?>?

  fun findAllByStatus(status: Int): List<OrderEntity?>?
  fun findAllByAuditCreatedByAndStatus(userId: String?, status: Int): List<OrderEntity?>?
  fun findAllByEstablishment(establishment: EstablishmentEntity?): List<OrderEntity?>?
  fun findAllByEstablishmentAndStatus(establishment: EstablishmentEntity?, status: Int): List<OrderEntity?>?
}
