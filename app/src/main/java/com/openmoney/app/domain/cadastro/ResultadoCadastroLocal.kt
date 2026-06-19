package com.openmoney.app.domain.cadastro

import com.openmoney.app.domain.model.Usuario

sealed interface ResultadoCadastroLocal {
    data class Sucesso(val usuario: Usuario) : ResultadoCadastroLocal
    data class Erro(val mensagem: String) : ResultadoCadastroLocal
}
