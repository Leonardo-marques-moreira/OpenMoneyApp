package com.openmoney.app.data.repositorio

import com.openmoney.app.data.local.armazenamento.ArmazenamentoUsuariosArquivo
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.domain.repositorio.RepositorioUsuario

class RepositorioUsuarioLocal(
    private val armazenamento: ArmazenamentoUsuariosArquivo,
) : RepositorioUsuario {

    override fun salvar(usuario: Usuario): Usuario {
        val usuariosAtuais = armazenamento.lerUsuarios()
        val novoUsuario = usuario.copy(id = (usuariosAtuais.maxOfOrNull { it.id } ?: 0L) + 1L)
        armazenamento.salvarUsuarios(usuariosAtuais + novoUsuario)
        return novoUsuario
    }

    override fun buscarPorEmail(email: String): Usuario? {
        val emailNormalizado = email.trim().lowercase()
        return armazenamento.lerUsuarios().firstOrNull { it.email == emailNormalizado }
    }

    override fun existeEmail(email: String): Boolean {
        return buscarPorEmail(email) != null
    }
}
