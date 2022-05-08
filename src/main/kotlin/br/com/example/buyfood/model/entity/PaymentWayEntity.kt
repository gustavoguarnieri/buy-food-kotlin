package br.com.example.buyfood.model.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "payment_way")
class PaymentWayEntity : BaseDescriptionEntity(), Serializable
