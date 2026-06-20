package com.openmoney.app.domain.comum

object ValidadorDadosUsuario {
    private val temLetra = Regex(".*[A-Za-z].*")
    private val temNumero = Regex(".*\\d.*")

    fun validarNomeObrigatorio(nome: String): String? {
        return if (nome.isBlank()) {
            "Campo obrigatório: nome."
        } else {
            null
        }
    }

    fun validarEmailObrigatorio(email: String): String? {
        return when {
            email.isBlank() -> "Campo obrigatório: e-mail."
            !email.contains("@") -> "E-mail inválido: $email"
            else -> null
        }
    }

    fun validarFormatoSenha(senha: String): String? {
        return if (
            senha.length < 8 ||
            !senha.matches(temLetra) ||
            !senha.matches(temNumero)
        ) {
            "A senha deve ter no mínimo 8 caracteres, contendo letras e números."
        } else {
            null
        }
    }
}
