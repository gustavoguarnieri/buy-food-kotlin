package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.BusinessHoursRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentBusinessHoursResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentBusinessHoursService
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
@RequestMapping("/api/v1/establishments")
class EstablishmentBusinessHoursController(
  private val establishmentBusinessHoursService: EstablishmentBusinessHoursService
) {

  private val log = KotlinLogging.logger {}

  @GetMapping("/{establishmentId}/business-hours")
  @ApiOperation(value = "Returns a list of business hours")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of business hours",
        response = EstablishmentBusinessHoursResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getBusinessHoursList(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestParam(required = false) status: Int?
  ): List<EstablishmentBusinessHoursResponseDTO?>? {
    log.info(
      "getBusinessHoursList: starting consult the list of business hours, establishmentId=$establishmentId"
    )
    return establishmentBusinessHoursService.getBusinessHoursList(establishmentId, status).also {
      log.info(
        "getBusinessHoursList: finished to consult the list of business hours, " +
          "establishmentId=$establishmentId"
      )
    }
  }

  @GetMapping("/{establishmentId}/business-hours/{businessHoursId}")
  @ApiOperation(value = "Returns the informed business hours")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed business hours",
        response = EstablishmentBusinessHoursResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getBusinessHours(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("businessHoursId") businessHoursId: @Valid @NotBlank Long
  ): EstablishmentBusinessHoursResponseDTO? {
    log.info(
      "getBusinessHours: starting to consult business hours by establishmentId=$establishmentId, " +
        "businessHoursId=$businessHoursId"
    )
    return establishmentBusinessHoursService.getBusinessHours(establishmentId, businessHoursId).also {
      log.info(
        "getBusinessHours: finished to consult business hours by establishmentId=$establishmentId, " +
          "businessHoursId=$businessHoursId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @GetMapping("/business-hours/mine")
  @ApiOperation(value = "Returns my business-hours establishment")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns my business-hours establishment",
        response = EstablishmentBusinessHoursResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getMyBusinessHoursList(
    @RequestParam(required = false) status: Int?
  ): List<EstablishmentBusinessHoursResponseDTO?>? {
    log.info("getMyBusinessHoursList: starting to consult my business hours")
    return establishmentBusinessHoursService.getMyBusinessHoursList(status).also {
      log.info("getMyBusinessHoursList: finished to consult my business hours")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping("/{establishmentId}/business-hours")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new business hours")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created business hours",
        response = EstablishmentBusinessHoursResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createBusinessHours(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestBody businessHoursRequestDto: @Valid BusinessHoursRequestDTO
  ): EstablishmentBusinessHoursResponseDTO? {
    log.info("createBusinessHours: starting to create new business hours, establishmentId=$establishmentId")
    return establishmentBusinessHoursService.createBusinessHours(establishmentId, businessHoursRequestDto).also {
      log.info(
        "createBusinessHours: finished to create new business hours, " +
          "establishmentId=$establishmentId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{establishmentId}/business-hours/{businessHoursId}")
  @ApiOperation(value = "Update business hours")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated business hours"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateBusinessHours(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("businessHoursId") businessHoursId: @Valid @NotBlank Long,
    @RequestBody businessHoursRequestDto: @Valid BusinessHoursRequestDTO
  ) {
    log.info(
      "updateBusinessHours: starting update business hours establishmentId=$establishmentId, " +
        "businessHoursId=$businessHoursId"
    )
    establishmentBusinessHoursService.updateBusinessHours(establishmentId, businessHoursId, businessHoursRequestDto)
    log.info(
      "updateBusinessHours: finished update business hours establishmentId=$establishmentId, " +
        "businessHoursId=$businessHoursId"
    )
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{establishmentId}/business-hours/{businessHoursId}")
  @ApiOperation(value = "Delete business hours")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted business hours"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteBusinessHours(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("businessHoursId") businessHoursId: @Valid @NotBlank Long
  ) {
    log.info(
      "deleteBusinessHours: starting delete business hours establishmentId=$establishmentId, " +
        "businessHoursId=$businessHoursId"
    )
    establishmentBusinessHoursService.deleteBusinessHours((establishmentId), (businessHoursId))
    log.info(
      "deleteBusinessHours: finished delete business hours establishmentId=$establishmentId, " +
        "businessHoursId=$businessHoursId"
    )
  }
}
