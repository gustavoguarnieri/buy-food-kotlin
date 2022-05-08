package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepository : JpaRepository<ImageEntity?, Long?> {
  fun findAllByProductIdAndStatus(productId: Long?, status: Int): List<ImageEntity?>?
  fun findAllByProductId(productId: Long?): List<ImageEntity?>?
}
