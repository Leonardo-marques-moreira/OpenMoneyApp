package com.openmoney.app.domain.meta

import com.openmoney.app.domain.model.MetaEconomia
import com.openmoney.app.domain.repositorio.RepositorioMetaEconomia
import java.math.BigDecimal
import java.time.LocalDate

class ServicoMetaEconomiaLocal(
    private val repositorioMetaEconomia: RepositorioMetaEconomia,
) {

    fun cadastrar(
        usuarioId: Long?,
        nomeMeta: String,
        valorMeta: BigDecimal,
        valorAtual: BigDecimal,
        dataLimite: LocalDate?,
    ): ResultadoCadastroMetaEconomiaLocal {
        if (usuarioId == null || usuarioId <= 0L) {
            return ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "Usuario autenticado obrigatorio para cadastrar meta.",
            )
        }

        if (nomeMeta.isBlank()) {
            return ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "Nome da meta obrigatorio.",
            )
        }

        if (valorMeta.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "O valor total da meta precisa ser maior que zero.",
            )
        }

        if (valorAtual.compareTo(BigDecimal.ZERO) < 0) {
            return ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "O valor atual nao pode ser negativo.",
            )
        }

        if (dataLimite == null) {
            return ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "Data limite obrigatoria.",
            )
        }

        val hoje = LocalDate.now()
        if (dataLimite.isBefore(hoje)) {
            return ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "A data limite deve ser hoje ou uma data futura.",
            )
        }

        val metaEconomia = MetaEconomia(
            id = 0L,
            nome = nomeMeta.trim(),
            valorMeta = valorMeta,
            valorAtual = valorAtual,
            dataCriacao = hoje,
            dataLimite = dataLimite,
            usuarioId = usuarioId,
        )

        return runCatching {
            val metaSalva = repositorioMetaEconomia.salvar(metaEconomia)
            ResultadoCadastroMetaEconomiaLocal.Sucesso(metaEconomia = metaSalva)
        }.getOrElse {
            ResultadoCadastroMetaEconomiaLocal.Erro(
                mensagem = "Nao foi possivel cadastrar a meta.",
            )
        }
    }

    fun listarPorUsuario(usuarioId: Long): List<MetaEconomia> {
        return repositorioMetaEconomia.listarPorUsuario(usuarioId)
    }
}
