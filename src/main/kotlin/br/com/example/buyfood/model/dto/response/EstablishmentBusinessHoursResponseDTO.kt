package br.com.example.buyfood.model.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "establishment")
class EstablishmentBusinessHoursResponseDTO {
  var id: Long? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeFirstPeriodSaturday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeFirstPeriodSaturday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var startTimeSecondPeriodSaturday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  var finalTimeSecondPeriodSaturday: LocalTime? = null

  var status = 1
}
