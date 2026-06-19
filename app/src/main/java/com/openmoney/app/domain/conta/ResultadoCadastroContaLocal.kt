package com.openmoney.app.domain.conta

import com.openmoney.app.domain.model.Conta

sealed interface ResultadoCadastroContaLocal {
    data class Sucesso(
        val conta: Conta,
    ) : ResultadoCadastroContaLocal

    data class Erro(
        val mensagem: String,
    ) : ResultadoCadastroContaLocal
}