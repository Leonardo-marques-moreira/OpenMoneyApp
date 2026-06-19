package com.openmoney.app.domain.model

import java.math.BigDecimal

data class Conta(
    val id: Long,
    val nome: String,
    val tipo: TipoConta,
    val cor: String,
    val saldo: BigDecimal,
    val usuarioId: Long,
)