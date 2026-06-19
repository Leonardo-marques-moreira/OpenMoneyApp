package com.openmoney.app.data.local.armazenamento

import android.content.Context
import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.TipoTransacao
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException

class ArmazenamentoCategoriasArquivo(
    private val context: Context,
) {
    private val nomeArquivo = "categorias_open_money.json"

    fun lerCategorias(): List<Categoria> {
        val conteudo = try {
            context.openFileInput(nomeArquivo).bufferedReader().use { it.readText() }
        } catch (_: FileNotFoundException) {
            return emptyList()
        }

        if (conteudo.isBlank()) {
            return emptyList()
        }

        val categoriasJson = JSONArray(conteudo)
        return buildList {
            for (indice in 0 until categoriasJson.length()) {
                add(categoriasJson.getJSONObject(indice).toCategoria())
            }
        }
    }

    fun salvarCategorias(categorias: List<Categoria>) {
        val categoriasJson = JSONArray()
        categorias.sortedBy { it.id }.forEach { categoria ->
            categoriasJson.put(categoria.toJson())
        }

        context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(categoriasJson.toString())
        }
    }

    private fun JSONObject.toCategoria(): Categoria {
        return Categoria(
            id = getLong("id"),
            nome = getString("nome"),
            tipo = TipoTransacao.valueOf(getString("tipo")),
            icone = getString("icone"),
            cor = getString("cor"),
            usuarioId = getLong("usuarioId"),
        )
    }

    private fun Categoria.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("nome", nome)
            .put("tipo", tipo.name)
            .put("icone", icone)
            .put("cor", cor)
            .put("usuarioId", usuarioId)
    }
}