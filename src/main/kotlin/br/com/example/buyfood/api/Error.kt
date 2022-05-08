package br.com.example.buyfood.api

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.OffsetDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class Error(
  private val statusCode: Int,
  private val description: String? = null,
  private val dateTime: OffsetDateTime,
  private val fields: List<Field>? = null
) {
  class Field(private val name: String, private val message: String)
}
