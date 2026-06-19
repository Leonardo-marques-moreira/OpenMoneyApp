package com.openmoney.app.data.local.armazenamento

import android.content.Context
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.model.Transacao
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.math.BigDecimal
import java.time.LocalDate

class ArmazenamentoTransacoesArquivo(
    private val context: Context,
) {
    private val nomeArquivo = "transacoes_open_money.json"

    fun lerTransacoes(): List<Transacao> {
        val conteudo = try {
            context.openFileInput(nomeArquivo).bufferedReader().use { it.readText() }
        } catch (_: FileNotFoundException) {
            return emptyList()
        }

        if (conteudo.isBlank()) {
            return emptyList()
        }

        val transacoesJson = JSONArray(conteudo)
        return buildList {
            for (indice in 0 until transacoesJson.length()) {
                add(transacoesJson.getJSONObject(indice).toTransacao())
            }
        }
    }

    fun salvarTransacoes(transacoes: List<Transacao>) {
        val transacoesJson = JSONArray()
        transacoes.sortedBy { it.id }.forEach { transacao ->
            transacoesJson.put(transacao.toJson())
        }

        context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(transacoesJson.toString())
        }
    }

    private fun JSONObject.toTransacao(): Transacao {
        return Transacao(
            id = getLong("id"),
            descricao = getString("descricao"),
            valor = BigDecimal(getString("valor")),
            tipo = TipoTransacao.valueOf(getString("tipo")),
            data = LocalDate.parse(getString("data")),
            contaId = getLong("contaId"),
            categoriaId = getLong("categoriaId"),
        )
    }

    private fun Transacao.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("descricao", descricao)
            .put("valor", valor.toPlainString())
            .put("tipo", tipo.name)
            .put("data", data.toString())
            .put("contaId", contaId)
            .put("categoriaId", categoriaId)
    }
}
