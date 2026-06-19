package com.openmoney.app.data.repositorio

import com.openmoney.app.data.local.armazenamento.ArmazenamentoMetasArquivo
import com.openmoney.app.domain.model.MetaEconomia
import com.openmoney.app.domain.repositorio.RepositorioMetaEconomia

class RepositorioMetaEconomiaLocal(
    private val armazenamento: ArmazenamentoMetasArquivo,
) : RepositorioMetaEconomia {

    override fun salvar(metaEconomia: MetaEconomia): MetaEconomia {
        val metasAtuais = armazenamento.lerMetas()
        val novaMeta = metaEconomia.copy(id = (metasAtuais.maxOfOrNull { it.id } ?: 0L) + 1L)
        armazenamento.salvarMetas(metasAtuais + novaMeta)
        return novaMeta
    }

    override fun atualizar(metaEconomia: MetaEconomia): MetaEconomia {
        val metasAtuais = armazenamento.lerMetas()
        require(metasAtuais.any { it.id == metaEconomia.id }) {
            "Meta nao encontrada para atualizacao."
        }

        val metasAtualizadas = metasAtuais.map { metaAtual ->
            if (metaAtual.id == metaEconomia.id) {
                metaEconomia
            } else {
                metaAtual
            }
        }

        armazenamento.salvarMetas(metasAtualizadas)
        return metaEconomia
    }

    override fun buscarPorId(id: Long): MetaEconomia? {
        return armazenamento.lerMetas().firstOrNull { it.id == id }
    }

    override fun listarPorUsuario(usuarioId: Long): List<MetaEconomia> {
        return armazenamento.lerMetas()
            .filter { it.usuarioId == usuarioId }
            .sortedWith(compareBy(MetaEconomia::dataLimite, MetaEconomia::id))
    }
}
