package com.openmoney.app.data.local.armazenamento

import android.content.Context
import com.openmoney.app.domain.model.MetaEconomia
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.math.BigDecimal
import java.time.LocalDate

class ArmazenamentoMetasArquivo(
    private val context: Context,
) {
    private val nomeArquivo = "metas_open_money.json"

    fun lerMetas(): List<MetaEconomia> {
        val conteudo = try {
            context.openFileInput(nomeArquivo).bufferedReader().use { it.readText() }
        } catch (_: FileNotFoundException) {
            return emptyList()
        }

        if (conteudo.isBlank()) {
            return emptyList()
        }

        val metasJson = JSONArray(conteudo)
        return buildList {
            for (indice in 0 until metasJson.length()) {
                add(metasJson.getJSONObject(indice).toMetaEconomia())
            }
        }
    }

    fun salvarMetas(metas: List<MetaEconomia>) {
        val metasJson = JSONArray()
        metas.sortedBy { it.id }.forEach { meta ->
            metasJson.put(meta.toJson())
        }

        context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(metasJson.toString())
        }
    }

    private fun JSONObject.toMetaEconomia(): MetaEconomia {
        return MetaEconomia(
            id = getLong("id"),
            nome = getString("nome"),
            valorMeta = BigDecimal(getString("valorMeta")),
            valorAtual = BigDecimal(getString("valorAtual")),
            dataCriacao = LocalDate.parse(getString("dataCriacao")),
            dataLimite = LocalDate.parse(getString("dataLimite")),
            usuarioId = getLong("usuarioId"),
        )
    }

    private fun MetaEconomia.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("nome", nome)
            .put("valorMeta", valorMeta.toPlainString())
            .put("valorAtual", valorAtual.toPlainString())
            .put("dataCriacao", dataCriacao.toString())
            .put("dataLimite", dataLimite.toString())
            .put("usuarioId", usuarioId)
    }
}
