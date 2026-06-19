package com.openmoney.app.domain.cadastro

object ValidadorCadastroUsuario {
    private val temLetra = Regex(".*[A-Za-z].*")
    private val temNumero = Regex(".*\\d.*")

    fun validar(
        nome: String,
        email: String,
        senha: String,
        confirmarSenha: String,
    ): ResultadoValidacaoCadastro {
        return when {
            nome.isBlank() -> ResultadoValidacaoCadastro(erroNome = "Campo obrigatório: nome.")
            email.isBlank() -> ResultadoValidacaoCadastro(erroEmail = "Campo obrigatório: e-mail.")
            !email.contains("@") -> ResultadoValidacaoCadastro(erroEmail = "E-mail inválido: $email")
            senha.isBlank() -> ResultadoValidacaoCadastro(erroSenha = "Campo obrigatório: senha.")
            senha.length < 8 || !senha.matches(temLetra) || !senha.matches(temNumero) -> {
                ResultadoValidacaoCadastro(
                    erroSenha = "A senha deve ter no mínimo 8 caracteres, contendo letras e números."
                )
            }
            confirmarSenha.isBlank() -> {
                ResultadoValidacaoCadastro(erroConfirmarSenha = "Campo obrigatório: confirmar senha.")
            }
            senha != confirmarSenha -> {
                ResultadoValidacaoCadastro(erroConfirmarSenha = "As senhas informadas não coincidem.")
            }
            else -> ResultadoValidacaoCadastro()
        }
    }
}
