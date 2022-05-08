package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EstablishmentImageRepository : JpaRepository<ImageEntity?, Long?> {
  fun findAllByEstablishmentIdAndStatus(establishmentId: Long?, status: Int): List<ImageEntity?>?
  fun findAllByEstablishmentId(establishmentId: Long?): List<ImageEntity?>?
  fun findByIdAndEstablishmentId(imageId: Long?, establishmentId: Long?): ImageEntity?
}
