package com.openmoney.app.data.repositorio

import com.openmoney.app.data.local.armazenamento.ArmazenamentoTransacoesArquivo
import com.openmoney.app.domain.model.Transacao
import com.openmoney.app.domain.repositorio.RepositorioTransacao

class RepositorioTransacaoLocal(
    private val armazenamento: ArmazenamentoTransacoesArquivo,
) : RepositorioTransacao {

    override fun salvar(transacao: Transacao): Transacao {
        val transacoesAtuais = armazenamento.lerTransacoes()
        val novaTransacao = transacao.copy(id = (transacoesAtuais.maxOfOrNull { it.id } ?: 0L) + 1L)
        armazenamento.salvarTransacoes(transacoesAtuais + novaTransacao)
        return novaTransacao
    }

    override fun listarPorContas(contaIds: Set<Long>): List<Transacao> {
        if (contaIds.isEmpty()) {
            return emptyList()
        }

        return armazenamento.lerTransacoes()
            .filter { it.contaId in contaIds }
            .sortedWith(compareByDescending<Transacao> { it.data }.thenByDescending { it.id })
    }
}
