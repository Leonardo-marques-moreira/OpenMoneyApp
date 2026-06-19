package com.openmoney.app.domain.transacao

import com.openmoney.app.domain.comum.ConversorEntradaFinanceira
import com.openmoney.app.domain.model.TipoTransacao
import java.math.BigDecimal

object ValidadorCadastroTransacao {

    fun validar(
        tipoTransacao: TipoTransacao?,
        valorInformado: String,
        descricao: String,
        dataInformada: String,
        contaId: Long?,
        categoriaId: Long?,
    ): ResultadoValidacaoTransacao {
        val erroTipoTransacao = if (tipoTransacao == null) {
            "Tipo de transacao obrigatorio."
        } else {
            null
        }

        val valorConvertido = ConversorEntradaFinanceira.converterValorMonetario(valorInformado)
        val erroValor = when {
            valorInformado.isBlank() -> "Informe o valor da transacao."
            valorConvertido == null -> "Informe um valor numerico valido."
            valorConvertido.compareTo(BigDecimal.ZERO) <= 0 -> "O valor precisa ser maior que zero."
            else -> null
        }

        val erroDescricao = if (descricao.isBlank()) {
            "Descricao obrigatoria."
        } else {
            null
        }

        val erroCategoria = if (categoriaId == null || categoriaId <= 0L) {
            "Selecione uma categoria."
        } else {
            null
        }

        val erroConta = if (contaId == null || contaId <= 0L) {
            "Selecione uma conta."
        } else {
            null
        }

        val dataConvertida = ConversorEntradaFinanceira.converterDataBrasil(dataInformada)
        val erroData = when {
            dataInformada.isBlank() -> "Informe a data da transacao."
            dataConvertida == null -> "Informe uma data valida no formato DD/MM/AAAA."
            else -> null
        }

        return ResultadoValidacaoTransacao(
            erroTipoTransacao = erroTipoTransacao,
            erroValor = erroValor,
            erroDescricao = erroDescricao,
            erroCategoria = erroCategoria,
            erroConta = erroConta,
            erroData = erroData,
            valorNormalizado = if (erroValor == null) valorConvertido else null,
            dataNormalizada = if (erroData == null) dataConvertida else null,
        )
    }
}
