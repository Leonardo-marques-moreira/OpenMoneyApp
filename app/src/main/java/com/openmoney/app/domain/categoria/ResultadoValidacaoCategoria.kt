package com.openmoney.app.domain.categoria

data class ResultadoValidacaoCategoria(
    val erroNomeCategoria: String? = null,
    val erroTipoCategoria: String? = null,
) {
    val temErro: Boolean
        get() = erroNomeCategoria != null || erroTipoCategoria != null
}