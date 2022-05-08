package br.com.example.buyfood.model.dto.response

class EstablishmentResponseDTO {
  var id: Long? = null
  var companyName: String? = null
  var tradingName: String? = null
  var email: String? = null
  var commercialPhone: String? = null
  var mobilePhone: String? = null
  var category: EstablishmentCategoryResponseDTO? = null
  var businessHours: EstablishmentBusinessHoursResponseDTO? = null
  var deliveryTax: EstablishmentDeliveryTaxResponseDTO? = null
  var status = 0
}
