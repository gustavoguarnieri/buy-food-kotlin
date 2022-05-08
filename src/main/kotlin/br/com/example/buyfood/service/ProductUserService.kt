package br.com.example.buyfood.service

import br.com.example.buyfood.model.dto.response.product.EstablishmentProductResponseDTO
import br.com.example.buyfood.model.entity.ProductEntity
import br.com.example.buyfood.model.repository.ProductRepository
import br.com.example.buyfood.util.StatusValidation
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class ProductUserService(
  private val modelMapper: ModelMapper,
  private val productRepository: ProductRepository,
  private val statusValidation: StatusValidation
) {

  fun getProductList(status: Int?): List<EstablishmentProductResponseDTO>? {
    return if (status == null) {
      productRepository.findAll().map { convertToEstablishmentProductResponseDto(it) }
    } else {
      productRepository
        .findAllByStatus(statusValidation.getStatusIdentification(status))
        ?.map { convertToEstablishmentProductResponseDto(it) }
    }
  }

  private fun convertToEstablishmentProductResponseDto(
    productEntity: ProductEntity?
  ): EstablishmentProductResponseDTO {
    return modelMapper.map(productEntity, EstablishmentProductResponseDTO::class.java)
  }
}
