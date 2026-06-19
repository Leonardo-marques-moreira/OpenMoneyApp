package com.openmoney.app.ui.meta

import com.openmoney.app.domain.model.MetaEconomia

data class EstadoTelaMeta(
    val destinoAtual: DestinoMeta = DestinoMeta.LISTA_METAS,
    val metas: List<MetaEconomia> = emptyList(),
    val nomeMeta: String = "",
    val valorMeta: String = "",
    val valorAtual: String = "",
    val dataLimite: String = "",
    val carregando: Boolean = false,
    val erroNomeMeta: String? = null,
    val erroValorMeta: String? = null,
    val erroValorAtual: String? = null,
    val erroDataLimite: String? = null,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null,
)
