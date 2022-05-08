package br.com.example.buyfood.model.dto.request

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ImageRequestDTO(
  val id: Long,
  val fileName: String? = null,
  val fileUri: String? = null,
  val fileType: String? = null,
  val size: Long = 0,
  val status: Int = 1
)
