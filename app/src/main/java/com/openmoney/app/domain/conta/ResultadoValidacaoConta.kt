package com.openmoney.app.domain.conta

import java.math.BigDecimal

data class ResultadoValidacaoConta(
    val erroNomeConta: String? = null,
    val erroTipoConta: String? = null,
    val erroSaldoInicial: String? = null,
    val saldoNormalizado: BigDecimal? = null,
) {
    val temErro: Boolean
        get() = erroNomeConta != null || erroTipoConta != null || erroSaldoInicial != null
}