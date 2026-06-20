package com.openmoney.app.domain.repositorio

import com.openmoney.app.domain.model.Usuario

interface RepositorioUsuario {
    fun salvar(usuario: Usuario): Usuario
    fun atualizar(usuario: Usuario): Usuario
    fun buscarPorEmail(email: String): Usuario?
    fun existeEmail(email: String): Boolean
}
