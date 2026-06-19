package com.openmoney.app.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class MetaEconomia(
    val id: Long,
    val nome: String,
    val valorMeta: BigDecimal,
    val valorAtual: BigDecimal,
    val dataCriacao: LocalDate,
    val dataLimite: LocalDate,
    val usuarioId: Long,
)
