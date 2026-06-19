package com.openmoney.app.ui.conta

import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoConta

data class EstadoTelaConta(
    val destinoAtual: DestinoConta = DestinoConta.LISTA_CONTAS,
    val contas: List<Conta> = emptyList(),
    val nomeConta: String = "",
    val tipoContaSelecionado: TipoConta? = null,
    val corSelecionada: String? = null,
    val saldoInicial: String = "",
    val carregando: Boolean = false,
    val erroNomeConta: String? = null,
    val erroTipoConta: String? = null,
    val erroSaldoInicial: String? = null,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null,
)