package com.openmoney.app.domain.transacao

import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.model.Transacao
import com.openmoney.app.domain.repositorio.RepositorioCategoria
import com.openmoney.app.domain.repositorio.RepositorioConta
import com.openmoney.app.domain.repositorio.RepositorioTransacao
import java.math.BigDecimal
import java.time.LocalDate

class ServicoTransacaoLocal(
    private val repositorioTransacao: RepositorioTransacao,
    private val repositorioConta: RepositorioConta,
    private val repositorioCategoria: RepositorioCategoria,
) {

    fun cadastrar(
        usuarioId: Long?,
        tipoTransacao: TipoTransacao?,
        valor: BigDecimal,
        descricao: String,
        data: LocalDate?,
        contaId: Long?,
        categoriaId: Long?,
    ): ResultadoCadastroTransacaoLocal {
        if (usuarioId == null || usuarioId <= 0L) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Usuario autenticado obrigatorio para registrar transacao.",
            )
        }

        if (tipoTransacao == null) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Tipo de transacao obrigatorio.",
            )
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "O valor da transacao precisa ser maior que zero.",
            )
        }

        if (descricao.isBlank()) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Descricao obrigatoria.",
            )
        }

        if (data == null) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Data da transacao obrigatoria.",
            )
        }

        if (contaId == null || contaId <= 0L) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Selecione uma conta valida.",
            )
        }

        if (categoriaId == null || categoriaId <= 0L) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Selecione uma categoria valida.",
            )
        }

        val conta = repositorioConta.buscarPorId(contaId)
            ?: return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Conta nao encontrada.",
            )

        if (conta.usuarioId != usuarioId) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "A conta selecionada nao pertence ao usuario autenticado.",
            )
        }

        val categoria = repositorioCategoria.buscarPorId(categoriaId)
            ?: return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Categoria nao encontrada.",
            )

        if (categoria.usuarioId != usuarioId) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "A categoria selecionada nao pertence ao usuario autenticado.",
            )
        }

        if (categoria.tipo != tipoTransacao) {
            return ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "A categoria selecionada nao corresponde ao tipo da transacao.",
            )
        }

        val saldoAtualizado = when (tipoTransacao) {
            TipoTransacao.RECEITA -> conta.saldo + valor
            TipoTransacao.DESPESA -> conta.saldo - valor
        }

        val contaAtualizada = conta.copy(saldo = saldoAtualizado)

        return runCatching {
            repositorioConta.atualizar(contaAtualizada)

            val novaTransacao = Transacao(
                id = 0L,
                descricao = descricao.trim(),
                valor = valor,
                tipo = tipoTransacao,
                data = data,
                contaId = conta.id,
                categoriaId = categoria.id,
            )

            val transacaoSalva = repositorioTransacao.salvar(novaTransacao)
            ResultadoCadastroTransacaoLocal.Sucesso(transacao = transacaoSalva)
        }.getOrElse {
            runCatching { repositorioConta.atualizar(conta) }
            ResultadoCadastroTransacaoLocal.Erro(
                mensagem = "Nao foi possivel registrar a transacao.",
            )
        }
    }

    fun listarPorUsuario(usuarioId: Long): List<Transacao> {
        val contaIds = repositorioConta.listarPorUsuario(usuarioId)
            .map { it.id }
            .toSet()

        return repositorioTransacao.listarPorContas(contaIds)
    }
}
