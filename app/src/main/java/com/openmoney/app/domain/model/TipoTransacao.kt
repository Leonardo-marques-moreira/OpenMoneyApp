package com.openmoney.app.domain.model

enum class TipoTransacao(
    val descricao: String,
) {
    RECEITA("Receita"),
    DESPESA("Despesa"),
}