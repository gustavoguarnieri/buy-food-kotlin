package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity?, Long?> {
  fun findAllByEstablishment(establishment: EstablishmentEntity?): List<ProductEntity?>?
  fun findAllByEstablishmentAndStatus(establishment: EstablishmentEntity?, status: Int): List<ProductEntity?>?
  fun findAllByStatus(status: Int): List<ProductEntity?>?
  fun findByEstablishmentAndId(establishment: EstablishmentEntity?, productId: Long?): ProductEntity?
}
