package com.openmoney.app.domain.model

data class Categoria(
    val id: Long,
    val nome: String,
    val tipo: TipoTransacao,
    val icone: String,
    val cor: String,
    val usuarioId: Long,
)