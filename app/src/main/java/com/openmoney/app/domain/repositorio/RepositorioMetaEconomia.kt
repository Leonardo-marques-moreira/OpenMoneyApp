package com.openmoney.app.domain.repositorio

import com.openmoney.app.domain.model.MetaEconomia

interface RepositorioMetaEconomia {
    fun salvar(metaEconomia: MetaEconomia): MetaEconomia
    fun atualizar(metaEconomia: MetaEconomia): MetaEconomia
    fun buscarPorId(id: Long): MetaEconomia?
    fun listarPorUsuario(usuarioId: Long): List<MetaEconomia>
}
