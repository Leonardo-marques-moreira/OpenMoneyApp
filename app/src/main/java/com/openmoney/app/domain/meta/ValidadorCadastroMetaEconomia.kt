package com.openmoney.app.domain.meta

import com.openmoney.app.domain.comum.ConversorEntradaFinanceira
import java.math.BigDecimal

object ValidadorCadastroMetaEconomia {

    fun validar(
        nomeMeta: String,
        valorMetaInformado: String,
        valorAtualInformado: String,
        dataLimiteInformada: String,
    ): ResultadoValidacaoMetaEconomia {
        val erroNomeMeta = if (nomeMeta.isBlank()) {
            "Informe o nome da meta."
        } else {
            null
        }

        val valorMetaConvertido = ConversorEntradaFinanceira.converterValorMonetario(valorMetaInformado)
        val erroValorMeta = when {
            valorMetaInformado.isBlank() -> "Informe o valor total da meta."
            valorMetaConvertido == null -> "Informe um valor numerico valido para a meta."
            valorMetaConvertido.compareTo(BigDecimal.ZERO) <= 0 -> "O valor total da meta precisa ser maior que zero."
            else -> null
        }

        val valorAtualConvertido = if (valorAtualInformado.isBlank()) {
            BigDecimal.ZERO
        } else {
            ConversorEntradaFinanceira.converterValorMonetario(valorAtualInformado)
        }
        val erroValorAtual = when {
            valorAtualConvertido == null -> "Informe um valor numerico valido para o valor atual."
            valorAtualConvertido.compareTo(BigDecimal.ZERO) < 0 -> "O valor atual nao pode ser negativo."
            else -> null
        }

        val dataLimiteConvertida = ConversorEntradaFinanceira.converterDataBrasil(dataLimiteInformada)
        val erroDataLimite = when {
            dataLimiteInformada.isBlank() -> "Informe a data limite da meta."
            dataLimiteConvertida == null -> "Informe uma data valida no formato DD/MM/AAAA."
            else -> null
        }

        return ResultadoValidacaoMetaEconomia(
            erroNomeMeta = erroNomeMeta,
            erroValorMeta = erroValorMeta,
            erroValorAtual = erroValorAtual,
            erroDataLimite = erroDataLimite,
            valorMetaNormalizado = if (erroValorMeta == null) valorMetaConvertido else null,
            valorAtualNormalizado = if (erroValorAtual == null) valorAtualConvertido else null,
            dataLimiteNormalizada = if (erroDataLimite == null) dataLimiteConvertida else null,
        )
    }
}
