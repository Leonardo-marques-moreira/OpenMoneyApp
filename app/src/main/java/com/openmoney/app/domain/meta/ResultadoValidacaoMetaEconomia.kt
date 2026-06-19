package com.openmoney.app.domain.meta

import java.math.BigDecimal
import java.time.LocalDate

data class ResultadoValidacaoMetaEconomia(
    val erroNomeMeta: String? = null,
    val erroValorMeta: String? = null,
    val erroValorAtual: String? = null,
    val erroDataLimite: String? = null,
    val valorMetaNormalizado: BigDecimal? = null,
    val valorAtualNormalizado: BigDecimal? = null,
    val dataLimiteNormalizada: LocalDate? = null,
) {
    val temErro: Boolean
        get() = erroNomeMeta != null ||
            erroValorMeta != null ||
            erroValorAtual != null ||
            erroDataLimite != null
}
