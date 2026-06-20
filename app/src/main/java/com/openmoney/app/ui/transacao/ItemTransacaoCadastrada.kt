package com.openmoney.app.ui.transacao

import com.openmoney.app.domain.model.TipoTransacao
import java.math.BigDecimal
import java.time.LocalDate

data class ItemTransacaoCadastrada(
    val id: Long,
    val descricao: String,
    val valor: BigDecimal,
    val tipo: TipoTransacao,
    val data: LocalDate,
    val nomeConta: String,
    val nomeCategoria: String,
    val codigoIconeCategoria: String,
    val corCategoria: String,
)
