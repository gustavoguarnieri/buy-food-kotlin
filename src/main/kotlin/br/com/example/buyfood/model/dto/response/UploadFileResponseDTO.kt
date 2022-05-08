package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
open class UploadFileResponseDTO(
  var fileName: String? = null,
  var fileUri: String? = null,
  var fileType: String? = null,
  var size: Long = 0
)
