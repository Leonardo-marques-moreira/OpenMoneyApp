package com.openmoney.app.domain.autenticacao

import com.openmoney.app.domain.model.Usuario

sealed interface ResultadoAutenticacaoLocal {
    data class Sucesso(val usuario: Usuario) : ResultadoAutenticacaoLocal
    data class Erro(val mensagem: String) : ResultadoAutenticacaoLocal
}
