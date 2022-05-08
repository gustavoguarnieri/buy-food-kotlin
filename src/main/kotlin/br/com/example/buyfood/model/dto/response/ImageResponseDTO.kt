package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ImageResponseDTO(
  var id: Long? = 0,
  var fileName: String? = null,
  var fileUri: String? = null,
  var fileType: String? = null,
  var size: Long = 0,
  var status: Int = 0
)
