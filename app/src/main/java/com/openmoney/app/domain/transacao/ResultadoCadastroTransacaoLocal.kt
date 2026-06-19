package com.openmoney.app.domain.transacao

import com.openmoney.app.domain.model.Transacao

sealed interface ResultadoCadastroTransacaoLocal {
    data class Sucesso(
        val transacao: Transacao,
    ) : ResultadoCadastroTransacaoLocal

    data class Erro(
        val mensagem: String,
    ) : ResultadoCadastroTransacaoLocal
}
