package com.openmoney.app.data.repositorio

import com.openmoney.app.data.local.armazenamento.ArmazenamentoCategoriasArquivo
import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.repositorio.RepositorioCategoria

class RepositorioCategoriaLocal(
    private val armazenamento: ArmazenamentoCategoriasArquivo,
) : RepositorioCategoria {

    override fun salvar(categoria: Categoria): Categoria {
        val categoriasAtuais = armazenamento.lerCategorias()
        val novaCategoria = categoria.copy(id = (categoriasAtuais.maxOfOrNull { it.id } ?: 0L) + 1L)
        armazenamento.salvarCategorias(categoriasAtuais + novaCategoria)
        return novaCategoria
    }

    override fun buscarPorId(id: Long): Categoria? {
        return armazenamento.lerCategorias().firstOrNull { it.id == id }
    }

    override fun listarPorUsuario(usuarioId: Long): List<Categoria> {
        return armazenamento.lerCategorias()
            .filter { it.usuarioId == usuarioId }
            .sortedWith(compareBy<Categoria> { it.tipo.name }.thenBy { it.nome.lowercase() })
    }

    override fun listarPorUsuarioETipo(usuarioId: Long, tipo: TipoTransacao): List<Categoria> {
        return listarPorUsuario(usuarioId).filter { it.tipo == tipo }
    }

    override fun existeCategoria(usuarioId: Long, nome: String, tipo: TipoTransacao): Boolean {
        val nomeNormalizado = nome.trim().lowercase()
        return armazenamento.lerCategorias().any { categoria ->
            categoria.usuarioId == usuarioId &&
                categoria.tipo == tipo &&
                categoria.nome.trim().lowercase() == nomeNormalizado
        }
    }
}
