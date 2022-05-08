package br.com.example.buyfood.exception

class NotFoundException(message: String) : BusinessException(message.ifBlank { "No data found" })
