package com.openmoney.app.domain.model

import java.time.LocalDate

data class Usuario(
    val id: Long,
    val nome: String,
    val email: String,
    val senha: String,
    val dataCadastro: LocalDate,
)
