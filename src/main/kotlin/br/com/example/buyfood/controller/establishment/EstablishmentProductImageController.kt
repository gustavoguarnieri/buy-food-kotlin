package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.ImageRequestDTO
import br.com.example.buyfood.model.dto.response.ImageResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentProductImageService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@CrossOrigin
@RestController
@RequestMapping("/api/v1/establishments/{establishmentId}/products/{productId}/images")
class EstablishmentProductImageController(private val productImageService: EstablishmentProductImageService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of product image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of product image",
        response = ImageResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getProductImageList(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @RequestParam(required = false) status: Int?
  ): List<ImageResponseDTO?>? {
    log.info(
      "getProductImageList: starting to consult the list of product image, " +
        "establishmentId=$establishmentId, productId=$productId"
    )
    return productImageService.getProductImageList(establishmentId, productId, status).also {
      log.info(
        "getProductImageList: finished to consult the list of product image, " +
          "establishmentId=$establishmentId, productId=$productId"
      )
    }
  }

  @GetMapping("/{imageId}")
  @ApiOperation(value = "Returns the informed product image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed product image",
        response = ImageResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getProductImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("imageId") imageId: @Valid @NotBlank Long
  ): ImageResponseDTO? {
    log.info(
      "getProductImage: starting to consult product image by " +
        "establishmentId=$establishmentId, productId=$productId, imageId=$imageId"
    )
    return productImageService.getProductImage(establishmentId, productId, imageId).also {
      log.info(
        "getProductImage: finished to consult product image by " +
          "establishmentId=$establishmentId, productId=$productId, imageId=$imageId"
      )
    }
  }

  @GetMapping("/download-file/{fileName}")
  @ApiOperation(value = "Returns the informed download product image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed download product image"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getDownloadProductImageList(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("fileName") fileName: @Valid @NotBlank String,
    request: HttpServletRequest
  ): ResponseEntity<Resource> {
    log.info(
      "getDownloadProductImageList: starting to consult product image by " +
        "establishmentId=$establishmentId, productId=$productId, fileName=$fileName"
    )
    return productImageService.getDownloadProductImage(productId, fileName, request).also {
      log.info(
        "getDownloadProductImageList: finished to consult product image by " +
          "establishmentId=$establishmentId, productId=$productId, fileName=$fileName"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping("/upload-file")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new product image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created product image",
        response = ImageResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createProductImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @RequestParam file: MultipartFile
  ): ImageResponseDTO? {
    log.info(
      "createProductImage: starting to create new product image, " +
        "establishmentId=$establishmentId, productId=$productId"
    )
    return productImageService.createProductImage(establishmentId, productId, file).also {
      log.info(
        "createProductImage: finished to create new product image, " +
          "establishmentId=$establishmentId, productId=$productId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping("/upload-files")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new product images")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created product images",
        response = ImageResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createProductImages(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @RequestParam files: Array<MultipartFile>
  ): List<ImageResponseDTO?>? {
    log.info(
      "createProductImages: starting to create new product images, " +
        "establishmentId=$establishmentId, productId=$productId"
    )
    return productImageService.createProductImageList((establishmentId), (productId), files).also {
      log.info(
        "createProductImages: finished to create new product images, " +
          "establishmentId=$establishmentId, productId=$productId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{imageId}")
  @ApiOperation(value = "Update product image")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated product image"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateProductImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("imageId") imageId: @Valid @NotBlank Long,
    @RequestBody imageRequestDto: @Valid ImageRequestDTO
  ) {
    log.info(
      "updateProductImage: starting update product image by " +
        "establishmentId=$establishmentId, productId=$productId, imageId=$imageId"
    )
    productImageService.updateProductImage(establishmentId, productId, imageId, imageRequestDto).also {
      log.info(
        "updateProductImage: finished update product image by " +
          "establishmentId=$establishmentId, productId=$productId, imageId=$imageId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{imageId}")
  @ApiOperation(value = "Delete product image")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted product image"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteProductImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("productId") productId: @Valid @NotBlank Long,
    @PathVariable("imageId") imageId: @Valid @NotBlank Long
  ) {
    log.info(
      "deleteProductImage: starting delete product image " +
        "establishmentId=$establishmentId, productId=$productId, imageId=$imageId"
    )
    productImageService.deleteProductImage((establishmentId), (productId), (imageId))
    log.info(
      "deleteProductImage: finished delete product image " +
        "establishmentId=$establishmentId, productId=$productId, imageId=$imageId"
    )
  }
}
