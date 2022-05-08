package br.com.example.buyfood.service

import br.com.example.buyfood.enums.PaymentStatus
import br.com.example.buyfood.model.repository.OrderRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DashboardService(private val orderRepository: OrderRepository) {

  fun getOrdersByMonthList() = orderRepository.findOrdersByMonth(LocalDateTime.now().minusDays(NINETY_DAYS.toLong()))
  fun getBillingByMonthList() =
    orderRepository.findBillingByMonth(LocalDateTime.now().minusDays(NINETY_DAYS.toLong()))

  fun getPreparationStatusList() =
    orderRepository.findPreparationStatus(LocalDateTime.now().minusDays(NINETY_DAYS.toLong()))

  fun getPaymentWayList() = orderRepository.findPaymentWay(LocalDateTime.now().minusDays(NINETY_DAYS.toLong()))
  fun getPaymentDeclinedStatusList() = orderRepository.findPaymentDeclinedStatus(
    LocalDateTime.now().minusDays(NINETY_DAYS.toLong()), PaymentStatus.DECLINED.name
  )

  companion object {
    private const val NINETY_DAYS = 90
  }
}
