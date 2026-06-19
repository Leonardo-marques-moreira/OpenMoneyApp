package com.openmoney.app.ui.painel

import com.openmoney.app.domain.model.TipoTransacao
import java.math.BigDecimal
import java.time.LocalDate

data class EstadoTelaDashboard(
    val saldoTotal: BigDecimal = BigDecimal.ZERO,
    val totalReceitas: BigDecimal = BigDecimal.ZERO,
    val totalDespesas: BigDecimal = BigDecimal.ZERO,
    val ultimasTransacoes: List<ItemUltimaTransacaoDashboard> = emptyList(),
    val carregando: Boolean = false,
)

data class ItemUltimaTransacaoDashboard(
    val id: Long,
    val descricao: String,
    val categoriaNome: String,
    val data: LocalDate,
    val valor: BigDecimal,
    val tipo: TipoTransacao,
    val codigoIconeCategoria: String,
    val corCategoria: String,
)
