package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.PaymentWayRequestDTO
import br.com.example.buyfood.model.dto.response.PaymentWayResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentPaymentWayService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
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
@RequestMapping("/api/v1/establishments/payment-way")
class EstablishmentPaymentWayController(private val paymentWayService: EstablishmentPaymentWayService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of payment way")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of payment way",
        response = PaymentWayResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPaymentWayList(@RequestParam(required = false) status: Int?): List<PaymentWayResponseDTO?>? {
    log.info("getPaymentWayList: starting to consult the list of payment way")
    return paymentWayService.getPaymentWayList(status).also {
      log.info("getPaymentWayList: finished to consult the list of payment way")
    }
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Returns the informed payment way")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed payment way",
        response = PaymentWayResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPaymentWay(@PathVariable("id") id: @Valid @NotBlank Long): PaymentWayResponseDTO? {
    log.info("getPaymentWay: starting to consult payment way by id=$id")
    return paymentWayService.getPaymentWay(id).also {
      log.info("getPaymentWay: finished to consult payment way by id=$id")
    }
  }

  @Secured("ROLE_ADMIN")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new payment way")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created payment way",
        response = PaymentWayResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createPaymentWay(@RequestBody paymentWayRequestDTO: @Valid PaymentWayRequestDTO): PaymentWayResponseDTO? {
    log.info("createPaymentWay: starting to create new payment way")
    return paymentWayService.createPaymentWay(paymentWayRequestDTO).also {
      log.info("createPaymentWay: finished to create new payment way")
    }
  }

  @Secured("ROLE_ADMIN")
  @PutMapping("/{id}")
  @ApiOperation(value = "Update payment way")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated payment way"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updatePaymentWay(
    @PathVariable("id") id: @Valid @NotBlank Long,
    @RequestBody paymentWayRequestDTO: @Valid PaymentWayRequestDTO
  ) {
    log.info("updatePaymentWay: starting update preparation status id=$id")
    paymentWayService.updatePaymentWay(id, paymentWayRequestDTO).also {
      log.info("updatePaymentWay: finished update preparation status id=$id")
    }
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete payment way")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted payment way"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deletePaymentWay(@PathVariable("id") id: @Valid @NotBlank Long) {
    log.info("deletePaymentWay: starting delete payment way id=$id")
    paymentWayService.deletePaymentWay(id).also {
      log.info("deletePaymentWay: finished delete payment way id=$id")
    }
  }
}
