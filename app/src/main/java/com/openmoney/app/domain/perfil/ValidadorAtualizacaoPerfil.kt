package com.openmoney.app.domain.perfil

import com.openmoney.app.domain.comum.ValidadorDadosUsuario

object ValidadorAtualizacaoPerfil {

    fun validar(
        nomeCompleto: String,
        email: String,
        senhaAtual: String,
        novaSenha: String,
        confirmarNovaSenha: String,
        senhaAtualCorreta: Boolean,
    ): ResultadoValidacaoPerfil {
        ValidadorDadosUsuario.validarNomeObrigatorio(nomeCompleto)?.let { erro ->
            return ResultadoValidacaoPerfil(erroNomeCompleto = erro)
        }

        ValidadorDadosUsuario.validarEmailObrigatorio(email)?.let { erro ->
            return ResultadoValidacaoPerfil(erroEmail = erro)
        }

        val desejaAlterarSenha = senhaAtual.isNotBlank() ||
            novaSenha.isNotBlank() ||
            confirmarNovaSenha.isNotBlank()

        if (!desejaAlterarSenha) {
            return ResultadoValidacaoPerfil()
        }

        if (senhaAtual.isBlank()) {
            return ResultadoValidacaoPerfil(
                erroSenhaAtual = "Campo obrigatório: senha atual.",
            )
        }

        if (!senhaAtualCorreta) {
            return ResultadoValidacaoPerfil(
                erroSenhaAtual = "Senha atual incorreta.",
            )
        }

        if (novaSenha.isBlank()) {
            return ResultadoValidacaoPerfil(
                erroNovaSenha = "Campo obrigatório: nova senha.",
            )
        }

        ValidadorDadosUsuario.validarFormatoSenha(novaSenha)?.let { erro ->
            return ResultadoValidacaoPerfil(erroNovaSenha = erro)
        }

        if (confirmarNovaSenha.isBlank()) {
            return ResultadoValidacaoPerfil(
                erroConfirmarNovaSenha = "Campo obrigatório: confirmar nova senha.",
            )
        }

        if (novaSenha != confirmarNovaSenha) {
            return ResultadoValidacaoPerfil(
                erroConfirmarNovaSenha = "As senhas informadas não coincidem.",
            )
        }

        return ResultadoValidacaoPerfil()
    }
}
