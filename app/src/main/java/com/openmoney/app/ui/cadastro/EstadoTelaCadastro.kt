package com.openmoney.app.ui.cadastro

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

data class EstadoTelaCadastro(
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val confirmarSenha: String = "",
    val senhaVisivel: Boolean = false,
    val confirmarSenhaVisivel: Boolean = false,
    val erroNome: String? = null,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val erroConfirmarSenha: String? = null,
    val mensagemCadastro: String? = null,
) {
    companion object {
        val Saver: Saver<EstadoTelaCadastro, Any> = listSaver(
            save = { estado ->
                listOf(
                    estado.nome,
                    estado.email,
                    estado.senha,
                    estado.confirmarSenha,
                    estado.senhaVisivel,
                    estado.confirmarSenhaVisivel,
                    estado.erroNome,
                    estado.erroEmail,
                    estado.erroSenha,
                    estado.erroConfirmarSenha,
                    estado.mensagemCadastro,
                )
            },
            restore = { valores ->
                EstadoTelaCadastro(
                    nome = valores[0] as String,
                    email = valores[1] as String,
                    senha = valores[2] as String,
                    confirmarSenha = valores[3] as String,
                    senhaVisivel = valores[4] as Boolean,
                    confirmarSenhaVisivel = valores[5] as Boolean,
                    erroNome = valores[6] as String?,
                    erroEmail = valores[7] as String?,
                    erroSenha = valores[8] as String?,
                    erroConfirmarSenha = valores[9] as String?,
                    mensagemCadastro = valores[10] as String?,
                )
            },
        )
    }
}
