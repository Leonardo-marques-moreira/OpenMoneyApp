package com.openmoney.app.domain.categoria

import com.openmoney.app.domain.model.Categoria

sealed interface ResultadoCadastroCategoriaLocal {
    data class Sucesso(
        val categoria: Categoria,
    ) : ResultadoCadastroCategoriaLocal

    data class Erro(
        val mensagem: String,
    ) : ResultadoCadastroCategoriaLocal
}