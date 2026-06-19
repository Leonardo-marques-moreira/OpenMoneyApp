package com.openmoney.app.domain.categoria

import com.openmoney.app.domain.model.TipoTransacao

object ValidadorCadastroCategoria {

    fun validar(
        nomeCategoria: String,
        tipoCategoria: TipoTransacao?,
    ): ResultadoValidacaoCategoria {
        val erroNomeCategoria = if (nomeCategoria.isBlank()) {
            "Nome da categoria obrigatorio."
        } else {
            null
        }

        val erroTipoCategoria = if (tipoCategoria == null) {
            "Tipo de categoria obrigatorio (RECEITA ou DESPESA)."
        } else {
            null
        }

        return ResultadoValidacaoCategoria(
            erroNomeCategoria = erroNomeCategoria,
            erroTipoCategoria = erroTipoCategoria,
        )
    }
}