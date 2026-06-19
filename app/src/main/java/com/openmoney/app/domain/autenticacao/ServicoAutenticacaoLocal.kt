package com.openmoney.app.domain.autenticacao

import com.openmoney.app.domain.cadastro.ResultadoCadastroLocal
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.domain.repositorio.RepositorioUsuario
import java.time.LocalDate

class ServicoAutenticacaoLocal(
    private val repositorioUsuario: RepositorioUsuario,
) {
    fun autenticar(email: String, senha: String): ResultadoAutenticacaoLocal {
        val emailNormalizado = email.trim().lowercase()
        val usuario = repositorioUsuario.buscarPorEmail(emailNormalizado)

        return if (usuario == null || usuario.senha != senha) {
            ResultadoAutenticacaoLocal.Erro("E-mail ou senha incorretos. Verifique suas credenciais.")
        } else {
            ResultadoAutenticacaoLocal.Sucesso(usuario)
        }
    }

    fun cadastrar(
        nome: String,
        email: String,
        senha: String,
    ): ResultadoCadastroLocal {
        val emailNormalizado = email.trim().lowercase()

        if (repositorioUsuario.existeEmail(emailNormalizado)) {
            return ResultadoCadastroLocal.Erro("Este e-mail já está cadastrado no sistema.")
        }

        val usuario = repositorioUsuario.salvar(
            Usuario(
                id = 0L,
                nome = nome.trim(),
                email = emailNormalizado,
                senha = senha,
                dataCadastro = LocalDate.now(),
            )
        )

        return ResultadoCadastroLocal.Sucesso(usuario)
    }
}
