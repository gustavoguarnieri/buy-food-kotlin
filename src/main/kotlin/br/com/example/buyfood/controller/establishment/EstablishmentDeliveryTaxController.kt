package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.EstablishmentDeliveryTaxPutRequestDTO
import br.com.example.buyfood.model.dto.request.EstablishmentDeliveryTaxRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentDeliveryTaxResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentDeliveryTaxService
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
@RequestMapping("/api/v1/establishments/delivery-tax")
class EstablishmentDeliveryTaxController(private val establishmentDeliveryTaxService: EstablishmentDeliveryTaxService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of delivery tax")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of delivery tax",
        response = EstablishmentDeliveryTaxResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thRrown")
    ]
  )
  fun getDeliveryTaxList(@RequestParam(required = false) status: Int?): List<EstablishmentDeliveryTaxResponseDTO?>? {
    log.info("getDeliveryTaxList: starting to consult the list of delivery tax")
    return establishmentDeliveryTaxService.getDeliveryTaxList(status).also {
      log.info("getDeliveryTaxList: finished to consult the list of delivery tax")
    }
  }

  @GetMapping("/{deliveryTaxId}")
  @ApiOperation(value = "Returns the informed delivery tax")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed delivery tax",
        response = EstablishmentDeliveryTaxResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getDeliveryTax(
    @PathVariable("deliveryTaxId") deliveryTaxId: @Valid @NotBlank Long
  ): EstablishmentDeliveryTaxResponseDTO? {
    log.info("getDeliveryTax: starting to consult delivery tax by deliveryTaxId=$deliveryTaxId")
    return establishmentDeliveryTaxService.getDeliveryTax(deliveryTaxId).also {
      log.info("getDeliveryTax: finished to consult delivery tax by deliveryTaxId=$deliveryTaxId")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @GetMapping("/mine")
  @ApiOperation(value = "Returns my establishment delivery tax")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns my establishment delivery tax",
        response = EstablishmentDeliveryTaxResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getMyDeliveryTax(@RequestParam(required = false) status: Int?): List<EstablishmentDeliveryTaxResponseDTO?>? {
    log.info("getMyDeliveryTax: starting to consult the list of my delivery tax")
    return establishmentDeliveryTaxService.getMyDeliveryTaxList(status).also {
      log.info("getMyDeliveryTax: finished to consult the list of my delivery tax")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new delivery tax")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created delivery tax",
        response = EstablishmentDeliveryTaxResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createDeliveryTax(
    @RequestBody establishmentDeliveryTaxRequestDto: @Valid EstablishmentDeliveryTaxRequestDTO
  ): EstablishmentDeliveryTaxResponseDTO? {
    log.info("createDeliveryTax: starting to create new delivery tax")
    return establishmentDeliveryTaxService.createDeliveryTax(establishmentDeliveryTaxRequestDto).also {
      log.info("createDeliveryTax: finished to create new delivery tax")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{deliveryTaxId}")
  @ApiOperation(value = "Update delivery tax")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated delivery tax"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateDeliveryTax(
    @PathVariable("deliveryTaxId") deliveryTaxId: @Valid @NotBlank Long,
    @RequestBody establishmentDeliveryTaxPutRequestDto: @Valid EstablishmentDeliveryTaxPutRequestDTO
  ) {
    log.info("updateDeliveryTax: starting update delivery tax deliveryTaxId=$deliveryTaxId")
    establishmentDeliveryTaxService.updateDeliveryTax(deliveryTaxId, establishmentDeliveryTaxPutRequestDto).also {
      log.info("updateDeliveryTax: finished update delivery tax deliveryTaxId=$deliveryTaxId")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{deliveryTaxId}")
  @ApiOperation(value = "Delete delivery tax")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted delivery tax"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteDeliveryTax(@PathVariable("deliveryTaxId") deliveryTaxId: @Valid @NotBlank Long) {
    log.info("deleteDeliveryTax: starting delete delivery tax deliveryTaxId=$deliveryTaxId")
    establishmentDeliveryTaxService.deleteDeliveryTax(deliveryTaxId).also {
      log.info("deleteDeliveryTax: finished delete delivery tax deliveryTaxId=$deliveryTaxId")
    }
  }
}
