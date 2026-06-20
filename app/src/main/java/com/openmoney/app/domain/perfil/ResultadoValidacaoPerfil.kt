package com.openmoney.app.domain.perfil

data class ResultadoValidacaoPerfil(
    val erroNomeCompleto: String? = null,
    val erroEmail: String? = null,
    val erroSenhaAtual: String? = null,
    val erroNovaSenha: String? = null,
    val erroConfirmarNovaSenha: String? = null,
) {
    val temErro: Boolean
        get() = listOf(
            erroNomeCompleto,
            erroEmail,
            erroSenhaAtual,
            erroNovaSenha,
            erroConfirmarNovaSenha,
        ).any { it != null }
}
