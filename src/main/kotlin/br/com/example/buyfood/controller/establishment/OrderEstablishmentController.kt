package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.OrderPutRequestDTO
import br.com.example.buyfood.model.dto.response.OrderResponseDTO
import br.com.example.buyfood.service.establishment.OrderEstablishmentService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@CrossOrigin
@RestController
@RequestMapping("/api/v1/establishments/orders")
class OrderEstablishmentController(
  private val orderEstablishmentService: OrderEstablishmentService
) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of orders")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of orders",
        response = OrderResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getOrderList(
    @RequestParam(required = false) status: Int?,
    @RequestParam(required = false) establishment: Int?
  ): List<OrderResponseDTO?>? {
    log.info("getOrderList: starting to consult the list of orders")
    return orderEstablishmentService.getOrderList(status, establishment).also {
      log.info("getOrderList: finished to consult the list of orders")
    }
  }

  @GetMapping("/{orderId}")
  @ApiOperation(value = "Returns the informed order")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed order",
        response = OrderResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getOrder(@PathVariable("orderId") orderId: @Valid @NotBlank Long): OrderResponseDTO? {
    log.info("getOrder: starting to consult order by orderId=$orderId")
    return orderEstablishmentService.getOrder(orderId).also {
      log.info("getOrder: finished to consult order by orderId=$orderId")
    }
  }

  @PutMapping("/{orderId}")
  @ApiOperation(value = "Update order")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated order"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateOrder(
    @PathVariable("orderId") orderId: @Valid @NotBlank Long,
    @RequestBody orderPutRequestDto: @Valid OrderPutRequestDTO
  ) {
    log.info("updateOrder: starting update order orderId=$orderId")
    orderEstablishmentService.updateOrder(orderId, orderPutRequestDto).also {
      log.info("updateOrder: finished update order orderId=$orderId")
    }
  }
}
