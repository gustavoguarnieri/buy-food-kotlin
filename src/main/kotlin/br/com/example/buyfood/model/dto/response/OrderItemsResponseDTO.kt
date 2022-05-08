package br.com.example.buyfood.model.dto.response

class OrderItemsResponseDTO {
  var id: Long? = null
  var lineCode = 0
  var product: ProductResponseDTO? = null
  var quantity: Int? = null
  var status = 0
}
