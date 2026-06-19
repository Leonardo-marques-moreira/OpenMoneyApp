package com.openmoney.app.data.local.armazenamento

import android.content.Context
import com.openmoney.app.domain.model.Usuario
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.time.LocalDate

class ArmazenamentoUsuariosArquivo(
    private val context: Context,
) {
    private val nomeArquivo = "usuarios_open_money.json"

    fun lerUsuarios(): List<Usuario> {
        val conteudo = try {
            context.openFileInput(nomeArquivo).bufferedReader().use { it.readText() }
        } catch (_: FileNotFoundException) {
            return emptyList()
        }

        if (conteudo.isBlank()) {
            return emptyList()
        }

        val usuariosJson = JSONArray(conteudo)
        return buildList {
            for (indice in 0 until usuariosJson.length()) {
                val usuarioJson = usuariosJson.getJSONObject(indice)
                add(usuarioJson.toUsuario())
            }
        }
    }

    fun salvarUsuarios(usuarios: List<Usuario>) {
        val usuariosJson = JSONArray()
        usuarios.sortedBy { it.id }.forEach { usuario ->
            usuariosJson.put(usuario.toJson())
        }

        context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(usuariosJson.toString())
        }
    }

    private fun JSONObject.toUsuario(): Usuario {
        return Usuario(
            id = getLong("id"),
            nome = getString("nome"),
            email = getString("email"),
            senha = getString("senha"),
            dataCadastro = LocalDate.parse(getString("dataCadastro")),
        )
    }

    private fun Usuario.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("nome", nome)
            .put("email", email)
            .put("senha", senha)
            .put("dataCadastro", dataCadastro.toString())
    }
}
