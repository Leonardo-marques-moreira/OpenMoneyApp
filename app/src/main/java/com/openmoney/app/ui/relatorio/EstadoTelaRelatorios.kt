package com.openmoney.app.ui.relatorio

import java.math.BigDecimal

data class EstadoTelaRelatorios(
    val saldoAtual: BigDecimal = BigDecimal.ZERO,
    val saldoMovimentado: BigDecimal = BigDecimal.ZERO,
    val totalReceitas: BigDecimal = BigDecimal.ZERO,
    val totalDespesas: BigDecimal = BigDecimal.ZERO,
    val receitasPorCategoria: List<ItemResumoCategoriaRelatorio> = emptyList(),
    val despesasPorCategoria: List<ItemResumoCategoriaRelatorio> = emptyList(),
    val carregando: Boolean = false,
)

data class ItemResumoCategoriaRelatorio(
    val categoriaNome: String,
    val corCategoria: String,
    val total: BigDecimal,
)
