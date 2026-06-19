package com.openmoney.app.data.repositorio

import com.openmoney.app.data.local.armazenamento.ArmazenamentoContasArquivo
import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.repositorio.RepositorioConta

class RepositorioContaLocal(
    private val armazenamento: ArmazenamentoContasArquivo,
) : RepositorioConta {

    override fun salvar(conta: Conta): Conta {
        val contasAtuais = armazenamento.lerContas()
        val novaConta = conta.copy(id = (contasAtuais.maxOfOrNull { it.id } ?: 0L) + 1L)
        armazenamento.salvarContas(contasAtuais + novaConta)
        return novaConta
    }

    override fun atualizar(conta: Conta): Conta {
        val contasAtuais = armazenamento.lerContas()
        require(contasAtuais.any { it.id == conta.id }) {
            "Conta nao encontrada para atualizacao."
        }

        val contasAtualizadas = contasAtuais.map { contaAtual ->
            if (contaAtual.id == conta.id) {
                conta
            } else {
                contaAtual
            }
        }

        armazenamento.salvarContas(contasAtualizadas)
        return conta
    }

    override fun buscarPorId(id: Long): Conta? {
        return armazenamento.lerContas().firstOrNull { it.id == id }
    }

    override fun listarPorUsuario(usuarioId: Long): List<Conta> {
        return armazenamento.lerContas()
            .filter { it.usuarioId == usuarioId }
            .sortedBy { it.id }
    }
}
