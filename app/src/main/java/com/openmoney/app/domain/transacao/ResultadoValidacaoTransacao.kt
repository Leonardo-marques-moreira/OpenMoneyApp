package com.openmoney.app.domain.transacao

import java.math.BigDecimal
import java.time.LocalDate

data class ResultadoValidacaoTransacao(
    val erroTipoTransacao: String? = null,
    val erroValor: String? = null,
    val erroDescricao: String? = null,
    val erroCategoria: String? = null,
    val erroConta: String? = null,
    val erroData: String? = null,
    val valorNormalizado: BigDecimal? = null,
    val dataNormalizada: LocalDate? = null,
) {
    val temErro: Boolean
        get() = erroTipoTransacao != null ||
            erroValor != null ||
            erroDescricao != null ||
            erroCategoria != null ||
            erroConta != null ||
            erroData != null
}
