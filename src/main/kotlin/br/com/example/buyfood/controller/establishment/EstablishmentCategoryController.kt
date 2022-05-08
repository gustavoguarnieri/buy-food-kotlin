package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.EstablishmentCategoryRequestDTO
import br.com.example.buyfood.model.dto.response.EstablishmentCategoryResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentCategoryService
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
@RequestMapping("/api/v1/establishments/categories")
class EstablishmentCategoryController(private val establishmentCategoryService: EstablishmentCategoryService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of establishment category")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of establishment category",
        response = EstablishmentCategoryResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getEstablishmentCategoryList(
    @RequestParam(required = false) status: Int?
  ): List<EstablishmentCategoryResponseDTO?>? {
    log.info("getEstablishmentCategoryList: starting to consult the list of establishment category")
    return establishmentCategoryService.getEstablishmentCategoryList(status).also {
      log.info("getEstablishmentCategoryList: finished to consult the list of establishment")
    }
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Returns the informed establishment category")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed establishment category",
        response = EstablishmentCategoryResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getEstablishmentCategory(
    @PathVariable("id") id: @Valid @NotBlank Long
  ): EstablishmentCategoryResponseDTO? {
    log.info("getEstablishmentCategory: starting to consult establishment category by id=$id")
    return establishmentCategoryService.getEstablishmentCategory(id).also {
      log.info("getEstablishmentCategory: finished to consult establishment by id=$id")
    }
  }

  @Secured("ROLE_ADMIN")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new establishment category")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created establishment category",
        response = EstablishmentCategoryResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createEstablishmentCategory(
    @RequestBody establishmentCategoryRequestDto: @Valid EstablishmentCategoryRequestDTO
  ): EstablishmentCategoryResponseDTO? {
    log.info("createEstablishmentCategory: starting to create new establishment category")
    return establishmentCategoryService.createEstablishmentCategory(establishmentCategoryRequestDto).also {
      log.info("createEstablishmentCategory: finished to create new establishment category")
    }
  }

  @Secured("ROLE_ADMIN")
  @PutMapping("/{id}")
  @ApiOperation(value = "Update establishment category")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated establishment category"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateEstablishmentCategory(
    @PathVariable("id") id: @Valid @NotBlank Long?,
    @RequestBody establishmentCategoryRequestDto: @Valid EstablishmentCategoryRequestDTO
  ) {
    log.info("updateEstablishmentCategory: starting update establishment category id=$id")
    return establishmentCategoryService.updateEstablishmentCategory(id, establishmentCategoryRequestDto).also {
      log.info("updateEstablishmentCategory: finished update establishment category id=$id")
    }
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete establishment category")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted establishment category"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteEstablishmentCategory(@PathVariable("id") id: @Valid @NotBlank Long) {
    log.info("deleteEstablishmentCategory: starting delete establishment category id=$id")
    return establishmentCategoryService.deleteEstablishmentCategory(id).also {
      log.info("deleteEstablishmentCategory: finished delete establishment category id=$id")
    }
  }
}
