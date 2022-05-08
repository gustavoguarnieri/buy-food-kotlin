package br.com.example.buyfood.controller

import br.com.example.buyfood.model.dto.request.DeliveryAddressRequestDTO
import br.com.example.buyfood.model.dto.response.DeliveryAddressResponseDTO
import br.com.example.buyfood.service.AddressService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.http.HttpStatus
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
@RequestMapping("/api/v1/users/addresses")
class AddressController(private val addressService: AddressService) {

  private val log = KotlinLogging.logger {}

  @GetMapping
  @ApiOperation(value = "Returns a list of user address")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of user address",
        response = DeliveryAddressResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getUserAddressList(
    @RequestParam(required = false) status: Int?
  ): List<DeliveryAddressResponseDTO?>? {
    log.info("getUserAddressList: starting to consult the list of user address")
    return addressService.getUserAddressList(status).also {
      log.info("getUserAddressList: finished to consult the list of user address")
    }
  }

  @GetMapping("/mine")
  @ApiOperation(value = "Returns a list of user address")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns a list of my user address",
        response = DeliveryAddressResponseDTO::class,
        responseContainer = "List"
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getUserAddressMineList(
    @RequestParam(required = false) status: Int?
  ): List<DeliveryAddressResponseDTO?>? {
    log.info("getUserAddressList: starting to consult the list of user address")
    return addressService.getAddressListByCreatedBy(status).also {
      log.info("getUserAddressList: finished to consult the list of user address")
    }
  }

  @GetMapping("/{addressId}")
  @ApiOperation(value = "Returns the informed user address")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 200,
        message = "Returns the informed user address",
        response = DeliveryAddressResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun getUserAddress(
    @PathVariable("addressId") addressId: @Valid @NotBlank Long
  ): DeliveryAddressResponseDTO? {
    log.info("getUserAddress: starting to consult user address by addressId=$addressId")
    return addressService.getUserAddress(addressId).also {
      log.info("getUserAddress: finished to consult user address by addressId=$addressId")
    }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create a new user address")
  @ApiResponses(
    value = [
      ApiResponse(
        code = 201,
        message = "Created user address",
        response = DeliveryAddressResponseDTO::class
      ),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun createUserAddress(
    @RequestBody deliveryAddressRequestDto: @Valid DeliveryAddressRequestDTO
  ): DeliveryAddressResponseDTO? {
    log.info("createUserAddress: starting to create new user address")
    return addressService.createUserAddress(deliveryAddressRequestDto).also {
      log.info("createUserAddress: finished to create new user address")
    }
  }

  @PutMapping("/{addressId}")
  @ApiOperation(value = "Update user address")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Updated user address"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun updateUserAddress(
    @PathVariable("addressId") addressId: @Valid @NotBlank Long,
    @RequestBody deliveryAddressRequestDto: @Valid DeliveryAddressRequestDTO
  ) {
    log.info("updateUserAddress: starting update user address by addressId=$addressId")
    addressService.updateUserAddress(addressId, deliveryAddressRequestDto).also {
      log.info("updateUserAddress: finished update user address by addressId=$addressId")
    }
  }

  @DeleteMapping("/{addressId}")
  @ApiOperation(value = "Delete user address")
  @ApiResponses(
    value = [
      ApiResponse(code = 200, message = "Deleted user address"),
      ApiResponse(code = 401, message = "You are unauthorized to access this resource"),
      ApiResponse(code = 403, message = "You do not have permission to access this resource"),
      ApiResponse(code = 500, message = "An exception was thrown")
    ]
  )
  fun deleteUserAddress(@PathVariable("addressId") addressId: @Valid @NotBlank Long) {
    log.info("deleteUserAddress: starting delete user address addressId=$addressId")
    addressService.deleteUserAddress(addressId).also {
      log.info("deleteUserAddress: finished delete user address addressId=$addressId")
    }
  }
}
