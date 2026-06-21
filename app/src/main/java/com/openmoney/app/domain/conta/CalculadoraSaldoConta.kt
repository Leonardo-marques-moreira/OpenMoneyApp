package com.openmoney.app.domain.conta

import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoConta
import com.openmoney.app.domain.model.TipoTransacao
import java.math.BigDecimal

object CalculadoraSaldoConta {

    fun calcularSaldoExibido(conta: Conta): BigDecimal {
        return when (conta.tipo) {
            TipoConta.CARTAO_CREDITO -> conta.saldo.negate()
            else -> conta.saldo
        }
    }

    fun calcularSaldoTotal(contas: List<Conta>): BigDecimal {
        return contas.fold(BigDecimal.ZERO) { acumulado, conta ->
            acumulado + calcularSaldoExibido(conta)
        }
    }

    fun atualizarSaldoAposTransacao(
        conta: Conta,
        tipoTransacao: TipoTransacao,
        valor: BigDecimal,
    ): BigDecimal {
        return when (conta.tipo) {
            TipoConta.CARTAO_CREDITO -> atualizarSaldoCartaoCredito(
                saldoAtual = conta.saldo,
                tipoTransacao = tipoTransacao,
                valor = valor,
            )

            else -> atualizarSaldoContaTradicional(
                saldoAtual = conta.saldo,
                tipoTransacao = tipoTransacao,
                valor = valor,
            )
        }
    }

    private fun atualizarSaldoContaTradicional(
        saldoAtual: BigDecimal,
        tipoTransacao: TipoTransacao,
        valor: BigDecimal,
    ): BigDecimal {
        return when (tipoTransacao) {
            TipoTransacao.RECEITA -> saldoAtual + valor
            TipoTransacao.DESPESA -> saldoAtual - valor
        }
    }

    private fun atualizarSaldoCartaoCredito(
        saldoAtual: BigDecimal,
        tipoTransacao: TipoTransacao,
        valor: BigDecimal,
    ): BigDecimal {
        return when (tipoTransacao) {
            TipoTransacao.RECEITA -> {
                val saldoAtualizado = saldoAtual - valor
                if (saldoAtualizado.compareTo(BigDecimal.ZERO) < 0) {
                    BigDecimal.ZERO
                } else {
                    saldoAtualizado
                }
            }

            TipoTransacao.DESPESA -> saldoAtual + valor
        }
    }
}
