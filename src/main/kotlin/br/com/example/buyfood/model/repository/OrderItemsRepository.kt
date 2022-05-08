package br.com.example.buyfood.model.repository

import br.com.example.buyfood.model.entity.OrderItemsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemsRepository : JpaRepository<OrderItemsEntity?, Long?>
