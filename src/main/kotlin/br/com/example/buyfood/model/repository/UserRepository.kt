package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity?, Long?> {
  fun findByEmail(email: String?): UserEntity?
  fun findByUserId(userId: String?): UserEntity?
}
