package br.com.example.buyfood.model.entity

import br.com.example.buyfood.enums.RegisterStatus
import br.com.example.buyfood.model.embeddable.AuditEmbeddable
import java.io.Serializable
import java.time.LocalTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "business_hours")
class BusinessHoursEntity : Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @OneToOne
  @JoinColumn(name = "establishment_id", referencedColumnName = "id")
  var establishment: EstablishmentEntity? = null

  var startTimeFirstPeriodSunday: LocalTime? = null
  var finalTimeFirstPeriodSunday: LocalTime? = null
  var startTimeSecondPeriodSunday: LocalTime? = null
  var finalTimeSecondPeriodSunday: LocalTime? = null
  var startTimeFirstPeriodMonday: LocalTime? = null
  var finalTimeFirstPeriodMonday: LocalTime? = null
  var startTimeSecondPeriodMonday: LocalTime? = null
  var finalTimeSecondPeriodMonday: LocalTime? = null
  var startTimeFirstPeriodTuesday: LocalTime? = null
  var finalTimeFirstPeriodTuesday: LocalTime? = null
  var startTimeSecondPeriodTuesday: LocalTime? = null
  var finalTimeSecondPeriodTuesday: LocalTime? = null
  var startTimeFirstPeriodWednesday: LocalTime? = null
  var finalTimeFirstPeriodWednesday: LocalTime? = null
  var startTimeSecondPeriodWednesday: LocalTime? = null
  var finalTimeSecondPeriodWednesday: LocalTime? = null
  var startTimeFirstPeriodThursday: LocalTime? = null
  var finalTimeFirstPeriodThursday: LocalTime? = null
  var startTimeSecondPeriodThursday: LocalTime? = null
  var finalTimeSecondPeriodThursday: LocalTime? = null
  var startTimeFirstPeriodFriday: LocalTime? = null
  var finalTimeFirstPeriodFriday: LocalTime? = null
  var startTimeSecondPeriodFriday: LocalTime? = null
  var finalTimeSecondPeriodFriday: LocalTime? = null
  var startTimeFirstPeriodSaturday: LocalTime? = null
  var finalTimeFirstPeriodSaturday: LocalTime? = null
  var startTimeSecondPeriodSaturday: LocalTime? = null
  var finalTimeSecondPeriodSaturday: LocalTime? = null

  @Column(nullable = false)
  var status = RegisterStatus.ENABLED.value

  @Embedded
  var audit = AuditEmbeddable()
}
