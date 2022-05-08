package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.ProductRequestDTO
import br.com.example.buyfood.model.dto.response.ProductResponseDTO
import br.com.example.buyfood.service.establishment.ProductEstablishmentService
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
@RequestMapping("/api/v1/establishments/{establishmentId}/products")
class ProductEstablishmentController(private val productEstablishmentService: ProductEstablishmentService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of product")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of product",
        response = ProductResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getProductListByEstablishment(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestParam(required = false) status: Int?
  ): List<ProductResponseDTO?>? {
    log.info("getProductList: starting to consult the list of product by establishmentId=$establishmentId")
    return productEstablishmentService.getProductListByEstablishment(establishmentId, status).also {
      log.info("getProductList: finished to consult the list of product by establishmentId=$establishmentId")
    }
  }

  @GetMapping("/{productId}")
  @ApiOperation(value = "Returns the informed product")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed product",
        response = ProductResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getProduct(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long
  ): ProductResponseDTO? {
    log.info(
      "getProduct: starting to consult product by establishmentId=$establishmentId, productId=$productId"
    )
    return productEstablishmentService.getProduct(establishmentId, productId).also {
      log.info(
        "getProduct: finished to consult product by establishmentId=$establishmentId, productId=$productId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new product")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created product",
        response = ProductResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createProduct(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestBody productRequestDto: @Valid ProductRequestDTO
  ): ProductResponseDTO? {
    log.info("createProduct: starting to create new product establishmentId=$establishmentId")
    return productEstablishmentService.createProduct(establishmentId, productRequestDto).also {
      log.info("createProduct: finished to create new product establishmentId=$establishmentId")
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{productId}")
  @ApiOperation(value = "Update product")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated product"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateProduct(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @RequestBody productRequestDto: @Valid ProductRequestDTO
  ) {
    log.info("updateProduct: starting update product establishmentId=$establishmentId, productId=$productId")
    productEstablishmentService.updateProduct(establishmentId, productId, productRequestDto).also {
      log.info(
        "updateProduct: finished update product establishmentId=$establishmentId, productId=$productId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{productId}")
  @ApiOperation(value = "Delete product")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted product"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteProduct(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long
  ) {
    log.info("deleteProduct: starting delete product establishmentId=$establishmentId, productId=$productId")
    productEstablishmentService.deleteProduct(establishmentId, productId).also {
      log.info(
        "deleteProduct: finished delete product establishmentId=$establishmentId, productId=$productId"
      )
    }
  }
}
