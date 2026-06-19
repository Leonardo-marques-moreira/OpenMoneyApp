package com.openmoney.app.domain.repositorio

import com.openmoney.app.domain.model.Conta

interface RepositorioConta {
    fun salvar(conta: Conta): Conta
    fun atualizar(conta: Conta): Conta
    fun buscarPorId(id: Long): Conta?
    fun listarPorUsuario(usuarioId: Long): List<Conta>
}
