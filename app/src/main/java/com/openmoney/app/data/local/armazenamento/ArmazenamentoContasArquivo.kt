package com.openmoney.app.data.local.armazenamento

import android.content.Context
import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoConta
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.math.BigDecimal

class ArmazenamentoContasArquivo(
    private val context: Context,
) {
    private val nomeArquivo = "contas_open_money.json"

    fun lerContas(): List<Conta> {
        val conteudo = try {
            context.openFileInput(nomeArquivo).bufferedReader().use { it.readText() }
        } catch (_: FileNotFoundException) {
            return emptyList()
        }

        if (conteudo.isBlank()) {
            return emptyList()
        }

        val contasJson = JSONArray(conteudo)
        return buildList {
            for (indice in 0 until contasJson.length()) {
                val contaJson = contasJson.getJSONObject(indice)
                add(contaJson.toConta())
            }
        }
    }

    fun salvarContas(contas: List<Conta>) {
        val contasJson = JSONArray()
        contas.sortedBy { it.id }.forEach { conta ->
            contasJson.put(conta.toJson())
        }

        context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(contasJson.toString())
        }
    }

    private fun JSONObject.toConta(): Conta {
        return Conta(
            id = getLong("id"),
            nome = getString("nome"),
            tipo = TipoConta.valueOf(getString("tipo")),
            cor = getString("cor"),
            saldo = BigDecimal(getString("saldo")),
            usuarioId = getLong("usuarioId"),
        )
    }

    private fun Conta.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("nome", nome)
            .put("tipo", tipo.name)
            .put("cor", cor)
            .put("saldo", saldo.toPlainString())
            .put("usuarioId", usuarioId)
    }
}