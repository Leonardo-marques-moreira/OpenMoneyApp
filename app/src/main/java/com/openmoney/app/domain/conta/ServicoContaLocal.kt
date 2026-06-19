package com.openmoney.app.domain.conta

import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoConta
import com.openmoney.app.domain.repositorio.RepositorioConta
import java.math.BigDecimal

class ServicoContaLocal(
    private val repositorioConta: RepositorioConta,
) {

    fun cadastrar(
        usuarioId: Long?,
        nomeConta: String,
        tipoConta: TipoConta?,
        corConta: String?,
        saldoInicial: BigDecimal,
    ): ResultadoCadastroContaLocal {
        if (usuarioId == null || usuarioId <= 0L) {
            return ResultadoCadastroContaLocal.Erro(
                mensagem = "Usuario autenticado obrigatorio para cadastrar conta.",
            )
        }

        if (nomeConta.isBlank()) {
            return ResultadoCadastroContaLocal.Erro(
                mensagem = "Campo obrigatorio: nome da conta.",
            )
        }

        if (tipoConta == null) {
            return ResultadoCadastroContaLocal.Erro(
                mensagem = "Tipo de conta obrigatorio.",
            )
        }

        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            return ResultadoCadastroContaLocal.Erro(
                mensagem = "Saldo inicial nao pode ser negativo.",
            )
        }

        val novaConta = Conta(
            id = 0L,
            nome = nomeConta.trim(),
            tipo = tipoConta,
            cor = corConta ?: tipoConta.corPadrao,
            saldo = saldoInicial,
            usuarioId = usuarioId,
        )

        return ResultadoCadastroContaLocal.Sucesso(
            conta = repositorioConta.salvar(novaConta),
        )
    }

    fun listarPorUsuario(usuarioId: Long): List<Conta> {
        return repositorioConta.listarPorUsuario(usuarioId)
    }
}