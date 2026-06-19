package com.openmoney.app.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Transacao(
    val id: Long,
    val descricao: String,
    val valor: BigDecimal,
    val tipo: TipoTransacao,
    val data: LocalDate,
    val contaId: Long,
    val categoriaId: Long,
)
