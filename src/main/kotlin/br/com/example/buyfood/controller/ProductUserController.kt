package br.com.example.buyfood.controller

import br.com.example.buyfood.model.dto.response.product.EstablishmentProductResponseDTO
import br.com.example.buyfood.service.ProductUserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users/products")
class ProductUserController(private val productUserService: ProductUserService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of product")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of product",
        response = EstablishmentProductResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getProductList(@RequestParam(required = false) status: Int?): List<EstablishmentProductResponseDTO?>? {
    log.info("getProductList: starting to consult the list of product")
    return productUserService.getProductList(status).also {
      log.info("getProductList: finished to consult the list of product")
    }
  }
}
