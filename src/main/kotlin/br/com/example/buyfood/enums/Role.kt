package br.com.example.buyfood.enums

import java.util.Arrays
import java.util.stream.Stream

enum class Role {
  ESTABLISHMENT, USER, ADMIN;

  companion object {
    fun stream(): Stream<Role> {
      return Arrays.stream(values())
    }
  }
}
