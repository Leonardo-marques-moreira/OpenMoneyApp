package com.openmoney.app.ui.perfil

data class EstadoTelaPerfil(
    val nomeCompleto: String = "",
    val email: String = "",
    val senhaAtual: String = "",
    val novaSenha: String = "",
    val confirmarNovaSenha: String = "",
    val senhaAtualVisivel: Boolean = false,
    val novaSenhaVisivel: Boolean = false,
    val confirmarNovaSenhaVisivel: Boolean = false,
    val erroNomeCompleto: String? = null,
    val erroEmail: String? = null,
    val erroSenhaAtual: String? = null,
    val erroNovaSenha: String? = null,
    val erroConfirmarNovaSenha: String? = null,
    val mensagemErro: String? = null,
    val mensagemSucesso: String? = null,
)
