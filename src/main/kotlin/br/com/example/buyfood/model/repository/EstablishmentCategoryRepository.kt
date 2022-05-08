package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.EstablishmentCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EstablishmentCategoryRepository : JpaRepository<EstablishmentCategoryEntity?, Long?> {
  fun findAllByStatus(status: Int): List<EstablishmentCategoryEntity?>?
}
