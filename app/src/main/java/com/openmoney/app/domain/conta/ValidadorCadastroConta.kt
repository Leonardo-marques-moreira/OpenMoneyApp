package com.openmoney.app.domain.conta

import com.openmoney.app.domain.comum.ConversorEntradaFinanceira
import com.openmoney.app.domain.model.TipoConta
import java.math.BigDecimal

object ValidadorCadastroConta {

    fun validar(
        nomeConta: String,
        tipoConta: TipoConta?,
        saldoInicial: String,
    ): ResultadoValidacaoConta {
        val erroNomeConta = if (nomeConta.isBlank()) {
            "Campo obrigatorio: nome da conta."
        } else {
            null
        }

        val erroTipoConta = if (tipoConta == null) {
            "Tipo de conta obrigatorio."
        } else {
            null
        }

        val saldoConvertido = when {
            saldoInicial.isBlank() -> BigDecimal.ZERO
            else -> ConversorEntradaFinanceira.converterValorMonetario(saldoInicial)
        }

        val erroSaldoInicial = when {
            saldoConvertido == null -> "Informe um valor numerico valido para o saldo inicial."
            saldoConvertido.compareTo(BigDecimal.ZERO) < 0 -> "Saldo inicial nao pode ser negativo."
            else -> null
        }

        return ResultadoValidacaoConta(
            erroNomeConta = erroNomeConta,
            erroTipoConta = erroTipoConta,
            erroSaldoInicial = erroSaldoInicial,
            saldoNormalizado = if (erroSaldoInicial == null) saldoConvertido else null,
        )
    }
}
