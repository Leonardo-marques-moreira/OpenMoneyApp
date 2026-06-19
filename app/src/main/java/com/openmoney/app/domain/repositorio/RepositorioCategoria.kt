package com.openmoney.app.domain.repositorio

import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.TipoTransacao

interface RepositorioCategoria {
    fun salvar(categoria: Categoria): Categoria
    fun buscarPorId(id: Long): Categoria?
    fun listarPorUsuario(usuarioId: Long): List<Categoria>
    fun listarPorUsuarioETipo(usuarioId: Long, tipo: TipoTransacao): List<Categoria>
    fun existeCategoria(usuarioId: Long, nome: String, tipo: TipoTransacao): Boolean
}
