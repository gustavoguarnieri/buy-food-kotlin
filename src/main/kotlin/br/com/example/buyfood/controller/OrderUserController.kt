package br.com.example.buyfood.controller

import br.com.example.buyfood.model.dto.request.OrderPutRequestDTO
import br.com.example.buyfood.model.dto.request.OrderRequestDTO
import br.com.example.buyfood.model.dto.response.OrderResponseDTO
import br.com.example.buyfood.service.OrderUserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users/orders")
class OrderUserController(private val orderUserService: OrderUserService) {

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
  fun getOrderList(@RequestParam(required = false) status: Int?): List<OrderResponseDTO?>? {
    log.info("getOrderList: starting to consult the list of orders")
    return orderUserService.getOrderList(status).also {
      log.info("getOrderList: finished to consult the list of orders")
    }
  }

  @GetMapping("/mine")
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
      ApiResponse(
        code = 403,
        message = "You do not have permission to access this resource"
      ),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getMyOrderList(@RequestParam(required = false) status: Int?): List<OrderResponseDTO?>? {
    log.info("getOrderList: starting to consult the list of orders")
    return orderUserService.getOrderListByCreatedBy(status).also {
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
    return orderUserService.getOrder(orderId).also {
      log.info("getOrder: finished to consult order by orderId=$orderId")
    }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new order")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created order",
        response = OrderResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createOrder(@RequestBody orderRequestDto: @Valid OrderRequestDTO): OrderResponseDTO? {
    log.info("createOrder: starting to create new order")
    return orderUserService.createOrder(orderRequestDto).also {
      log.info("createOrder: finished to create new order")
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
    orderUserService.updateOrder(orderId, orderPutRequestDto).also {
      log.info("updateOrder: finished update order orderId={}", orderId)
    }
  }

  @DeleteMapping("/{orderId}")
  @ApiOperation(value = "Delete order")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted order"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteOrder(@PathVariable("orderId") orderId: @Valid @NotBlank Long) {
    log.info("deleteOrder: starting delete order orderId=$orderId")
    orderUserService.deleteOrder(orderId).also {
      log.info("deleteOrder: finished delete order orderId=$orderId")
    }
  }
}
