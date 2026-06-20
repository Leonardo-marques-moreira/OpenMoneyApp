package com.openmoney.app.domain.perfil

import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.domain.repositorio.RepositorioUsuario

class ServicoPerfilLocal(
    private val repositorioUsuario: RepositorioUsuario,
) {

    fun atualizarPerfil(
        usuarioAtual: Usuario,
        nomeCompleto: String,
        email: String,
        novaSenha: String?,
    ): ResultadoAtualizacaoPerfilLocal {
        val emailNormalizado = email.trim().lowercase()
        val usuarioComMesmoEmail = repositorioUsuario.buscarPorEmail(emailNormalizado)

        if (usuarioComMesmoEmail != null && usuarioComMesmoEmail.id != usuarioAtual.id) {
            return ResultadoAtualizacaoPerfilLocal.Erro(
                "Este e-mail já está cadastrado no sistema.",
            )
        }

        val usuarioAtualizado = repositorioUsuario.atualizar(
            usuarioAtual.copy(
                nome = nomeCompleto.trim(),
                email = emailNormalizado,
                senha = novaSenha ?: usuarioAtual.senha,
            )
        )

        return ResultadoAtualizacaoPerfilLocal.Sucesso(usuarioAtualizado)
    }
}
