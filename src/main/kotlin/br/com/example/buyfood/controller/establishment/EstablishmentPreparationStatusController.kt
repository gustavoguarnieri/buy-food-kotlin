package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.PreparationStatusRequestDTO
import br.com.example.buyfood.model.dto.response.PreparationStatusResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentPreparationStatusService
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
@RequestMapping("/api/v1/establishments/preparation-status")
class EstablishmentPreparationStatusController(
  private val preparationStatusService: EstablishmentPreparationStatusService
) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of preparation status")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of preparation status",
        response = PreparationStatusResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPreparationStatusList(
    @RequestParam(required = false) status: Int?
  ): List<PreparationStatusResponseDTO?>? {
    log.info("getPreparationStatusList: starting to consult the list of preparation status")
    return preparationStatusService.getPreparationStatusList(status).also {
      log.info("getPreparationStatusList: finished to consult the list preparation status")
    }
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Returns the informed preparation status")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed preparation status",
        response = PreparationStatusResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getPreparationStatus(
    @PathVariable("id") id: @Valid @NotBlank Long
  ): PreparationStatusResponseDTO? {
    log.info("getPreparationStatus: starting to consult preparation status by id=$id")
    return preparationStatusService.getPreparationStatus(id).also {
      log.info("getPreparationStatus: finished to consult preparation status by id=$id")
    }
  }

  @Secured("ROLE_ADMIN")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new preparation status")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created preparation status",
        response = PreparationStatusResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createPreparationStatus(
    @RequestBody establishmentCategoryRequestDto: @Valid PreparationStatusRequestDTO
  ): PreparationStatusResponseDTO? {
    log.info("createPreparationStatus: starting to create new preparation status")
    return preparationStatusService.createPreparationStatus(establishmentCategoryRequestDto).also {
      log.info("createPreparationStatus: finished to create new preparation status")
    }
  }

  @Secured("ROLE_ADMIN")
  @PutMapping("/{id}")
  @ApiOperation(value = "Update preparation status")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated preparation status"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updatePreparationStatus(
    @PathVariable("id") id: @Valid @NotBlank Long,
    @RequestBody preparationStatusRequestDTO: @Valid PreparationStatusRequestDTO
  ) {
    log.info("updatePreparationStatus: starting update preparation status id=$id")
    preparationStatusService.updatePreparationStatus(id, preparationStatusRequestDTO).also {
      log.info("updatePreparationStatus: finished update preparation status id=$id")
    }
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete preparation status")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted preparation status"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deletePreparationStatus(@PathVariable("id") id: @Valid @NotBlank Long) {
    log.info("deletePreparationStatus: starting delete preparation status id=$id")
    preparationStatusService.deletePreparationStatus(id).also {
      log.info("deletePreparationStatus: finished delete preparation status id=$id")
    }
  }
}
