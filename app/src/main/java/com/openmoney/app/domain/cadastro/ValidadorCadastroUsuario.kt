package com.openmoney.app.domain.cadastro

import com.openmoney.app.domain.comum.ValidadorDadosUsuario

object ValidadorCadastroUsuario {
    fun validar(
        nome: String,
        email: String,
        senha: String,
        confirmarSenha: String,
    ): ResultadoValidacaoCadastro {
        ValidadorDadosUsuario.validarNomeObrigatorio(nome)?.let { erro ->
            return ResultadoValidacaoCadastro(erroNome = erro)
        }

        ValidadorDadosUsuario.validarEmailObrigatorio(email)?.let { erro ->
            return ResultadoValidacaoCadastro(erroEmail = erro)
        }

        if (senha.isBlank()) {
            return ResultadoValidacaoCadastro(erroSenha = "Campo obrigatório: senha.")
        }

        ValidadorDadosUsuario.validarFormatoSenha(senha)?.let { erro ->
            return ResultadoValidacaoCadastro(erroSenha = erro)
        }

        if (confirmarSenha.isBlank()) {
            return ResultadoValidacaoCadastro(
                erroConfirmarSenha = "Campo obrigatório: confirmar senha."
            )
        }

        if (senha != confirmarSenha) {
            return ResultadoValidacaoCadastro(
                erroConfirmarSenha = "As senhas informadas não coincidem."
            )
        }

        return ResultadoValidacaoCadastro()
    }
}
