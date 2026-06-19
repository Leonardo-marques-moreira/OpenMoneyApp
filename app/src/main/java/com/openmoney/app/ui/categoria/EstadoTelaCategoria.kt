package com.openmoney.app.ui.categoria

import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.ui.conta.CoresContaDisponiveis

data class EstadoTelaCategoria(
    val categorias: List<Categoria> = emptyList(),
    val tipoSelecionado: TipoTransacao = TipoTransacao.RECEITA,
    val nomeCategoria: String = "",
    val iconeSelecionado: String = IconesCategoriaDisponiveis.first().codigo,
    val corSelecionada: String = CoresContaDisponiveis.first(),
    val carregando: Boolean = false,
    val erroNomeCategoria: String? = null,
    val erroTipoCategoria: String? = null,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null,
)