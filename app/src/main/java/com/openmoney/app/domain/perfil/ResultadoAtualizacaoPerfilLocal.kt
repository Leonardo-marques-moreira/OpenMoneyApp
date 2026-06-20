package com.openmoney.app.domain.perfil

import com.openmoney.app.domain.model.Usuario

sealed interface ResultadoAtualizacaoPerfilLocal {
    data class Sucesso(val usuario: Usuario) : ResultadoAtualizacaoPerfilLocal
    data class Erro(val mensagem: String) : ResultadoAtualizacaoPerfilLocal
}
