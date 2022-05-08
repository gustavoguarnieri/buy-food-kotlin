package br.com.example.buyfood.controller.establishment

import br.com.example.buyfood.model.dto.request.ImageRequestDTO
import br.com.example.buyfood.model.dto.response.ImageResponseDTO
import br.com.example.buyfood.service.establishment.EstablishmentImageService
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
@RequestMapping("/api/v1/establishments/{establishmentId}/images")
class EstablishmentImageController(private val establishmentImageService: EstablishmentImageService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of establishment image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of establishment image",
        response = ImageResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getEstablishmentImageList(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestParam(required = false) status: Int?
  ): List<ImageResponseDTO?>? {
    log.info(
      "getEstablishmentImageList: starting to consult the list of establishment image, " +
        "establishmentId=$establishmentId"
    )
    return establishmentImageService.getEstablishmentImageList(establishmentId, status).also {
      log.info(
        "getEstablishmentImageList: finished to consult the list of establishment image, " +
          "establishmentId=$establishmentId"
      )
    }
  }

  @GetMapping("/{imageId}")
  @ApiOperation(value = "Returns the informed establishment image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed establishment image",
        response = ImageResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getEstablishmentImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("imageId") imageId: @Valid @NotBlank Long
  ): ImageResponseDTO? {
    log.info(
      "getEstablishmentImage: starting to consult establishment image by " +
        "establishmentId=$establishmentId, imageId=$imageId"
    )
    return establishmentImageService.getEstablishmentImage(establishmentId, imageId).also {
      log.info(
        "getEstablishmentImage: finished to consult establishment image by " +
          "establishmentId=$establishmentId, imageId=$imageId"
      )
    }
  }

  @GetMapping("/download-file/{fileName}")
  @ApiOperation(value = "Returns the informed download establishment image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed download establishment image"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getDownloadEstablishmentImageList(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("fileName") fileName: @Valid @NotBlank String,
    request: HttpServletRequest
  ): ResponseEntity<Resource> {
    log.info(
      "getDownloadEstablishmentImageList: starting to consult establishment image by " +
        "establishmentId=$establishmentId, fileName=$fileName"
    )
    return establishmentImageService.getDownloadEstablishmentImage(establishmentId, fileName, request).also {
      log.info(
        "getDownloadEstablishmentImageList: finished to consult establishment image by " +
          "establishmentId=$establishmentId, fileName=$fileName"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping("/upload-file")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new establishment image")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created establishment image",
        response = ImageResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createEstablishmentImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestParam file: MultipartFile
  ): ImageResponseDTO? {
    log.info(
      "createEstablishmentImage: starting to create new establishment image, " +
        "establishmentId=$establishmentId"
    )
    return establishmentImageService.createEstablishmentImage(establishmentId, file).also {
      log.info(
        "createEstablishmentImage: finished to create new establishment image, " +
          "establishmentId=$establishmentId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PostMapping("/upload-files")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new establishment images")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created establishment images",
        response = ImageResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createEstablishmentImages(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @RequestParam files: Array<MultipartFile>
  ): List<ImageResponseDTO?>? {
    log.info(
      "createEstablishmentImages: starting to create new establishment images, " +
        "establishmentId=$establishmentId"
    )
    return establishmentImageService.createEstablishmentImageList(establishmentId, files).also {
      log.info(
        "createEstablishmentImages: finished to create new establishment images, " +
          "establishmentId=$establishmentId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @PutMapping("/{imageId}")
  @ApiOperation(value = "Update establishment image")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated establishment image"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateEstablishmentImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("imageId") imageId: @Valid @NotBlank Long,
    @RequestBody imageRequestDto: @Valid ImageRequestDTO
  ) {
    log.info(
      "updateEstablishmentImage: starting update establishment image by " +
        "establishmentId=$establishmentId, imageId=$imageId"
    )
    establishmentImageService.updateEstablishmentImage(establishmentId, imageId, imageRequestDto).also {
      log.info(
        "updateEstablishmentImage: finished update establishment image by " +
          "establishmentId=$establishmentId, imageId=$imageId"
      )
    }
  }

  @Secured("ROLE_ESTABLISHMENT", "ROLE_ADMIN")
  @DeleteMapping("/{imageId}")
  @ApiOperation(value = "Delete establishment image")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted establishment image"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteEstablishmentImage(
    @PathVariable("establishmentId") establishmentId: @Valid @NotBlank Long,
    @PathVariable("imageId") imageId: @Valid @NotBlank Long
  ) {
    log.info(
      "deleteEstablishmentImage: starting delete establishment image " +
        "establishmentId=$establishmentId, imageId=$imageId"
    )
    establishmentImageService.deleteEstablishmentImage(establishmentId, imageId).also {
      log.info(
        "deleteEstablishmentImage: finished delete establishment image " +
          "establishmentId=$establishmentId, imageId=$imageId"
      )
    }
  }
}
