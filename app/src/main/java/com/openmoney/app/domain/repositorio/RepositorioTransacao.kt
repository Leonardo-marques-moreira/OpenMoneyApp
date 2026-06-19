package com.openmoney.app.domain.repositorio

import com.openmoney.app.domain.model.Transacao

interface RepositorioTransacao {
    fun salvar(transacao: Transacao): Transacao
    fun listarPorContas(contaIds: Set<Long>): List<Transacao>
}
