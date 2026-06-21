package com.openmoney.app.ui.relatorio

import java.math.BigDecimal

data class EstadoTelaRelatorios(
    val identificadorPeriodoSelecionado: String = "",
    val periodoReferencia: String = "",
    val periodosDisponiveis: List<ItemPeriodoRelatorio> = emptyList(),
    val saldoAtual: BigDecimal = BigDecimal.ZERO,
    val saldoMovimentado: BigDecimal = BigDecimal.ZERO,
    val totalReceitas: BigDecimal = BigDecimal.ZERO,
    val totalDespesas: BigDecimal = BigDecimal.ZERO,
    val receitasPorCategoria: List<ItemResumoCategoriaRelatorio> = emptyList(),
    val despesasPorCategoria: List<ItemResumoCategoriaRelatorio> = emptyList(),
    val comparativoMensal: List<ItemComparativoMensalRelatorio> = emptyList(),
    val carregando: Boolean = false,
)

data class ItemPeriodoRelatorio(
    val identificador: String,
    val rotulo: String,
)

data class ItemResumoCategoriaRelatorio(
    val categoriaNome: String,
    val corCategoria: String,
    val total: BigDecimal,
)

data class ItemComparativoMensalRelatorio(
    val rotuloMes: String,
    val totalReceitas: BigDecimal,
    val totalDespesas: BigDecimal,
    val emDestaque: Boolean,
)
