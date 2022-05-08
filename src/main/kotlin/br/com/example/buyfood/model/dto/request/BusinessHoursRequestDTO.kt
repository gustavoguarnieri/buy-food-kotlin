package br.com.example.buyfood.model.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import java.time.LocalTime

open class BusinessHoursRequestDTO {
  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodSunday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodMonday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodTuesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodWednesday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodThursday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodFriday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeFirstPeriodSaturday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeFirstPeriodSaturday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val startTimeSecondPeriodSaturday: LocalTime? = null

  @JsonDeserialize(using = LocalTimeDeserializer::class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  val finalTimeSecondPeriodSaturday: LocalTime? = null

  val status = 1
}
