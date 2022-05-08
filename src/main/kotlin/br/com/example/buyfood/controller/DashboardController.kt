package br.com.example.buyfood.controller

import br.com.example.buyfood.model.`interface`.DashboardStatisticsInterface
import br.com.example.buyfood.service.DashboardService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController(private val dashboardService: DashboardService) {

  private val log = KotlinLogging.logger {}

  @GetMapping("/admin/orders-by-month")
  @ApiOperation(value = "Returns a list of orders by month")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of orders by month",
        response = DashboardStatisticsInterface::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getOrdersByMonthList(): List<DashboardStatisticsInterface?>? {
    log.info("getOrdersByMonthList: starting to consult the list of orders by month")
    return dashboardService.getOrdersByMonthList().also {
      log.info("getOrdersByMonthList: finished to consult the list of orders by month")
    }
  }

  @GetMapping("/admin/billing-by-month")
  @ApiOperation(value = "Returns a list of billing by month")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of billing by month",
        response = DashboardStatisticsInterface::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getBillingByMonthList(): List<DashboardStatisticsInterface?>? {
    log.info("getBillingByMonthList: starting to consult the list of orders by month")
    return dashboardService.getBillingByMonthList().also {
      log.info("getBillingByMonthList: finished to consult the list of orders by month")
    }
  }

  @GetMapping("/admin/preparation-status")
  @ApiOperation(value = "Returns a list of preparation status")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of preparation status",
        response = DashboardStatisticsInterface::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPreparationStatusList(): List<DashboardStatisticsInterface?>? {
    log.info("getPreparationStatusList: starting to consult the preparation status list")
    return dashboardService.getPreparationStatusList().also {
      log.info("getPreparationStatusList: finished to consult the preparation status list")
    }
  }

  @GetMapping("/admin/payment-way")
  @ApiOperation(value = "Returns a list of payment way")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of payment way",
        response = DashboardStatisticsInterface::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPaymentWayList(): List<DashboardStatisticsInterface?>? {
    log.info("getPaymentWayList: starting to consult the payment way list")
    return dashboardService.getPaymentWayList().also {
      log.info("getPaymentWayList: finished to consult the payment way list")
    }
  }

  @GetMapping("/admin/payment-declined-status")
  @ApiOperation(value = "Returns a list of payment declined status")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of declined status",
        response = DashboardStatisticsInterface::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPaymentDeclinedStatusList(): List<DashboardStatisticsInterface?>? {
    log.info("getPaymentDeclinedStatusList: starting to consult the payment declined status list")
    return dashboardService.getPaymentDeclinedStatusList().also {
      log.info("getPaymentDeclinedStatusList: finished to consult the payment declined status list")
    }
  }
}
