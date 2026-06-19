package com.openmoney.app.ui.login

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

data class EstadoTelaLogin(
    val email: String = "",
    val senha: String = "",
    val senhaVisivel: Boolean = false,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val mensagemAutenticacao: String? = null,
    val mensagemSucesso: String? = null,
) {
    companion object {
        val Saver: Saver<EstadoTelaLogin, Any> = listSaver(
            save = { estado ->
                listOf(
                    estado.email,
                    estado.senha,
                    estado.senhaVisivel,
                    estado.erroEmail,
                    estado.erroSenha,
                    estado.mensagemAutenticacao,
                    estado.mensagemSucesso,
                )
            },
            restore = { valores ->
                EstadoTelaLogin(
                    email = valores[0] as String,
                    senha = valores[1] as String,
                    senhaVisivel = valores[2] as Boolean,
                    erroEmail = valores[3] as String?,
                    erroSenha = valores[4] as String?,
                    mensagemAutenticacao = valores[5] as String?,
                    mensagemSucesso = valores[6] as String?,
                )
            },
        )
    }
}
