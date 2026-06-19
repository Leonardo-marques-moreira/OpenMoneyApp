package com.openmoney.app.domain.model

enum class TipoConta(
    val descricao: String,
    val corPadrao: String,
) {
    CONTA_CORRENTE(
        descricao = "Conta Corrente",
        corPadrao = "#1E88E5",
    ),
    POUPANCA(
        descricao = "Poupanca",
        corPadrao = "#8E24AA",
    ),
    CARTEIRA(
        descricao = "Carteira",
        corPadrao = "#43A047",
    ),
    CARTAO_CREDITO(
        descricao = "Cartao de Credito",
        corPadrao = "#E53935",
    ),
}