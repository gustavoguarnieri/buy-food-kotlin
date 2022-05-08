package br.com.example.buyfood.model.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "preparation_status")
class PreparationStatusEntity : BaseDescriptionEntity(), Serializable
