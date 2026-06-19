package com.openmoney.app.domain.categoria

import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.repositorio.RepositorioCategoria

class ServicoCategoriaLocal(
    private val repositorioCategoria: RepositorioCategoria,
) {

    fun cadastrar(
        usuarioId: Long?,
        nomeCategoria: String,
        tipoCategoria: TipoTransacao?,
        iconeCategoria: String?,
        corCategoria: String?,
    ): ResultadoCadastroCategoriaLocal {
        if (usuarioId == null || usuarioId <= 0L) {
            return ResultadoCadastroCategoriaLocal.Erro(
                mensagem = "Usuario autenticado obrigatorio para cadastrar categoria.",
            )
        }

        if (nomeCategoria.isBlank()) {
            return ResultadoCadastroCategoriaLocal.Erro(
                mensagem = "Nome da categoria obrigatorio.",
            )
        }

        if (tipoCategoria == null) {
            return ResultadoCadastroCategoriaLocal.Erro(
                mensagem = "Tipo de categoria obrigatorio (RECEITA ou DESPESA).",
            )
        }

        if (repositorioCategoria.existeCategoria(usuarioId, nomeCategoria, tipoCategoria)) {
            return ResultadoCadastroCategoriaLocal.Erro(
                mensagem = "Ja existe uma categoria com esse nome para o tipo selecionado.",
            )
        }

        val novaCategoria = Categoria(
            id = 0L,
            nome = nomeCategoria.trim(),
            tipo = tipoCategoria,
            icone = iconeCategoria ?: "categoria",
            cor = corCategoria ?: "#25A67C",
            usuarioId = usuarioId,
        )

        return ResultadoCadastroCategoriaLocal.Sucesso(
            categoria = repositorioCategoria.salvar(novaCategoria),
        )
    }

    fun listarPorUsuario(usuarioId: Long): List<Categoria> {
        return repositorioCategoria.listarPorUsuario(usuarioId)
    }

    fun listarPorUsuarioETipo(
        usuarioId: Long,
        tipoTransacao: TipoTransacao,
    ): List<Categoria> {
        return repositorioCategoria.listarPorUsuarioETipo(usuarioId, tipoTransacao)
    }
}
