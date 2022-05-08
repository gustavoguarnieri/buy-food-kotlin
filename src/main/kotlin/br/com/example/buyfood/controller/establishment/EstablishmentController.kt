package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.EstablishmentRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentService
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
class EstablishmentController(private val establishmentService: EstablishmentService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of establishment")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of establishment",
        response = EstablishmentResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getEstablishmentList(@RequestParam(required = false) status: Int?): List<EstablishmentResponseDTO?>? {
    log.info("getEstablishmentList: starting to consult the list of establishment")
    return establishmentService.getEstablishmentList(status).also {
      log.info("getEstablishmentList: finished to consult the list of establishment")
    }
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Returns the informed establishment")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed establishment",
        response = EstablishmentResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getEstablishment(@PathVariable("id") id: @Valid @NotBlank Long): EstablishmentResponseDTO? {
    log.info("getEstablishment: starting to consult establishment by id=$id")
    return establishmentService.getEstablishment(id).also {
      log.info("getEstablishment: finished to consult establishment by id=$id")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @GetMapping("/mine")
  @ApiOperation(value = "Returns my establishment")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns my establishment",
        response = EstablishmentResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getMyEstablishmentList(@RequestParam(required = false) status: Int?): List<EstablishmentResponseDTO?>? {
    log.info("getMyEstablishmentList: starting to consult my establishment")
    return establishmentService.getMyEstablishmentList(status).also {
      log.info("getMyEstablishmentList: finished to consult my establishment")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new establishment")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created establishment",
        response = EstablishmentResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createEstablishment(
    @RequestBody establishmentRequestDto: @Valid EstablishmentRequestDTO
  ): EstablishmentResponseDTO? {
    log.info("createEstablishment: starting to create new establishment")
    return establishmentService.createEstablishment(establishmentRequestDto).also {
      log.info("createEstablishment: finished to create new establishment")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{id}")
  @ApiOperation(value = "Update establishment")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated establishment"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateEstablishment(
    @PathVariable("id") id: @Valid @NotBlank Long,
    @RequestBody establishmentRequestDto: @Valid EstablishmentRequestDTO
  ) {
    log.info("updateEstablishment: starting update establishment id=$id")
    return establishmentService.updateEstablishment(id, establishmentRequestDto).also {
      log.info("updateEstablishment: finished update establishment id=$id")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete establishment")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted establishment"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteEstablishment(@PathVariable("id") id: @Valid @NotBlank Long?) {
    log.info("deleteEstablishment: starting delete establishment id=$id")
    return establishmentService.deleteEstablishment(id!!).also {
      log.info("deleteEstablishment: finished delete establishment id=$id")
    }
  }
}
