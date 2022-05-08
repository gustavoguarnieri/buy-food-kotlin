package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.IngredientRequestDTO
import br.com.example.buyfood.model.dto.response.IngredientResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentIngredientService
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
@RequestMapping("/api/v1/establishments/{establishmentId}/products/{productId}/ingredients")
class EstablishmentIngredientController(private val ingredientService: EstablishmentIngredientService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a ingredient list of product")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a ingredient list of product",
        response = IngredientResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getIngredientList(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @RequestParam(required = false) status: Int?
  ): List<IngredientResponseDTO?>? {
    log.info(
      "getIngredientList: starting to consult the ingredient list " +
        "establishmentId=$establishmentId, productId=$productId, status=$status"
    )
    return ingredientService.getIngredientList(establishmentId, productId, status).also {
      log.info(
        "getIngredientList: finished to consult the ingredient list " +
          "establishmentId=$establishmentId, productId=$productId, status=$status"
      )
    }
  }

  @GetMapping("/{ingredientId}")
  @ApiOperation(value = "Returns the informed ingredient")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed ingredient",
        response = IngredientResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getIngredient(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("ingredientId") ingredientId: @Valid @NotBlank Long
  ): IngredientResponseDTO? {
    log.info(
      "getIngredient: starting to consult ingredient by " +
        "establishmentId=$establishmentId, productId=$productId, ingredientId=$ingredientId"
    )
    return ingredientService.getIngredient(establishmentId, productId, ingredientId).also {
      log.info(
        "getIngredient: finished to consult ingredient by " +
          "establishmentId=$establishmentId, productId=$productId, ingredientId=$ingredientId"
      )
    }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new ingredient")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created ingredient",
        response = IngredientResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createIngredient(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @RequestBody ingredientRequestDTO: @Valid IngredientRequestDTO
  ): IngredientResponseDTO? {
    log.info(
      "createIngredient: starting to create new ingredient by " +
        "establishmentId=$establishmentId, productId=$productId"
    )
    return ingredientService.createIngredient(establishmentId, productId, ingredientRequestDTO).also {
      log.info(
        "createIngredient: finished to create new ingredient by " +
          "establishmentId=$establishmentId, productId=$productId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{ingredientId}")
  @ApiOperation(value = "Update ingredient")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated ingredient"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateIngredient(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("ingredientId") ingredientId: @Valid @NotBlank Long,
    @RequestBody ingredientRequestDTO: @Valid IngredientRequestDTO
  ) {
    log.info(
      "updateIngredient: starting update ingredient " +
        "establishmentId=$establishmentId, productId=$productId, ingredientId=$ingredientId"
    )
    ingredientService.updateIngredient(establishmentId, productId, ingredientId, ingredientRequestDTO).also {
      log.info(
        "updateIngredient: finished update ingredient " +
          "establishmentId=$establishmentId, productId=$productId, ingredientId=$ingredientId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{ingredientId}")
  @ApiOperation(value = "Delete ingredient")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted ingredient"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteIngredient(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("ingredientId") ingredientId: @Valid @NotBlank Long
  ) {
    log.info(
      "deleteIngredient: starting delete ingredient " +
        "establishmentId=$establishmentId, productId=$productId, ingredientId=$ingredientId"
    )
    ingredientService.deleteIngredient(establishmentId, ingredientId).also {
      log.info(
        "deleteIngredient: finished delete ingredient " +
          "establishmentId=$establishmentId, productId=$productId, ingredientId=$ingredientId"
      )
    }
  }
}
