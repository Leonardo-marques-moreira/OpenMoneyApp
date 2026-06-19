package com.openmoney.app.domain.cadastro

data class ResultadoValidacaoCadastro(
    val erroNome: String? = null,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val erroConfirmarSenha: String? = null,
) {
    val temErro: Boolean
        get() = erroNome != null || erroEmail != null || erroSenha != null || erroConfirmarSenha != null
}
