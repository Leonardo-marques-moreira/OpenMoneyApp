package com.openmoney.app.domain.meta

import com.openmoney.app.domain.model.MetaEconomia

sealed interface ResultadoCadastroMetaEconomiaLocal {
    data class Sucesso(
        val metaEconomia: MetaEconomia,
    ) : ResultadoCadastroMetaEconomiaLocal

    data class Erro(
        val mensagem: String,
    ) : ResultadoCadastroMetaEconomiaLocal
}
