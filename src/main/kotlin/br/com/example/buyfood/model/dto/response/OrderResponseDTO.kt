package br.com.example.buyfood.model.dto.response

class OrderResponseDTO {
  var id: Long? = null
  var establishment: EstablishmentResponseDTO? = null
  var deliveryAddressId: Long? = null
  var delivery: EstablishmentDeliveryTaxResponseDTO? = null
  var items: List<OrderItemsResponseDTO>? = null
  var paymentWay: PaymentWayResponseDTO? = null
  var paymentStatus: String? = null
  var preparationStatus: PreparationStatusResponseDTO? = null
  var observation: String? = null
  var status = 0
}
