package com.openmoney.app.domain.autenticacao

data class ResultadoValidacaoLogin(
    val erroEmail: String? = null,
    val erroSenha: String? = null,
) {
    val temErro: Boolean
        get() = erroEmail != null || erroSenha != null
}

object ValidadorCamposLogin {
    fun validar(email: String, senha: String): ResultadoValidacaoLogin {
        return when {
            email.isBlank() -> ResultadoValidacaoLogin(erroEmail = "Campo obrigatório: e-mail.")
            senha.isBlank() -> ResultadoValidacaoLogin(erroSenha = "Campo obrigatório: senha.")
            else -> ResultadoValidacaoLogin()
        }
    }
}
