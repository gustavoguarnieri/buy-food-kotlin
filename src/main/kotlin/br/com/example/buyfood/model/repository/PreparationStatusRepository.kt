package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.PreparationStatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PreparationStatusRepository : JpaRepository<PreparationStatusEntity?, Long?> {
  fun findAllByStatus(status: Int): List<PreparationStatusEntity?>?
  fun findByDescriptionIgnoreCase(description: String?): PreparationStatusEntity?
}
