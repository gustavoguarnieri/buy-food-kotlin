package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.IngredientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : JpaRepository<IngredientEntity?, Long?> {
  fun findAllByProductIdAndStatus(productId: Long?, status: Int): List<IngredientEntity?>?
  fun findAllByProductId(productId: Long?): List<IngredientEntity?>?
}
