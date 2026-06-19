package com.openmoney.app.ui.transacao

import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoTransacao
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val formatadorDataTelaTransacao = DateTimeFormatter.ofPattern("dd/MM/uuuu")

private fun dataAtualFormatadaTelaTransacao(): String {
    return LocalDate.now().format(formatadorDataTelaTransacao)
}

data class EstadoTelaTransacao(
    val tipoSelecionado: TipoTransacao = TipoTransacao.RECEITA,
    val valor: String = "",
    val descricao: String = "",
    val dataLancamento: String = dataAtualFormatadaTelaTransacao(),
    val contaSelecionadaId: Long? = null,
    val categoriaSelecionadaId: Long? = null,
    val contasDisponiveis: List<Conta> = emptyList(),
    val categoriasDisponiveis: List<Categoria> = emptyList(),
    val carregando: Boolean = false,
    val erroTipoTransacao: String? = null,
    val erroValor: String? = null,
    val erroDescricao: String? = null,
    val erroCategoria: String? = null,
    val erroConta: String? = null,
    val erroData: String? = null,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null,
    val versaoSaldoContas: Long = 0L,
)
