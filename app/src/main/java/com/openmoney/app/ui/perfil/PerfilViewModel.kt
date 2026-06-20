package com.openmoney.app.ui.perfil

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openmoney.app.data.local.armazenamento.ArmazenamentoUsuariosArquivo
import com.openmoney.app.data.repositorio.RepositorioUsuarioLocal
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.domain.perfil.ResultadoAtualizacaoPerfilLocal
import com.openmoney.app.domain.perfil.ServicoPerfilLocal
import com.openmoney.app.domain.perfil.ValidadorAtualizacaoPerfil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilViewModel(
    private val servicoPerfil: ServicoPerfilLocal,
    usuarioAutenticado: Usuario,
) : ViewModel() {

    private var usuarioAutenticadoAtual = usuarioAutenticado

    var estado by mutableStateOf(
        EstadoTelaPerfil(
            nomeCompleto = usuarioAutenticado.nome,
            email = usuarioAutenticado.email,
        )
    )
        private set

    fun atualizarNomeCompleto(novoNome: String) {
        estado = estado.copy(
            nomeCompleto = novoNome,
            erroNomeCompleto = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarEmail(novoEmail: String) {
        estado = estado.copy(
            email = novoEmail,
            erroEmail = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarSenhaAtual(novaSenhaAtual: String) {
        estado = estado.copy(
            senhaAtual = novaSenhaAtual,
            erroSenhaAtual = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarNovaSenha(novaSenha: String) {
        estado = estado.copy(
            novaSenha = novaSenha,
            erroNovaSenha = null,
            erroConfirmarNovaSenha = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun atualizarConfirmarNovaSenha(novaConfirmacao: String) {
        estado = estado.copy(
            confirmarNovaSenha = novaConfirmacao,
            erroConfirmarNovaSenha = null,
            mensagemErro = null,
            mensagemSucesso = null,
        )
    }

    fun alternarVisibilidadeSenhaAtual() {
        estado = estado.copy(senhaAtualVisivel = !estado.senhaAtualVisivel)
    }

    fun alternarVisibilidadeNovaSenha() {
        estado = estado.copy(novaSenhaVisivel = !estado.novaSenhaVisivel)
    }

    fun alternarVisibilidadeConfirmarNovaSenha() {
        estado = estado.copy(
            confirmarNovaSenhaVisivel = !estado.confirmarNovaSenhaVisivel,
        )
    }

    fun salvarAlteracoes(aoPerfilAtualizado: (Usuario) -> Unit) {
        val estadoAtual = estado
        val resultadoValidacao = ValidadorAtualizacaoPerfil.validar(
            nomeCompleto = estadoAtual.nomeCompleto,
            email = estadoAtual.email,
            senhaAtual = estadoAtual.senhaAtual,
            novaSenha = estadoAtual.novaSenha,
            confirmarNovaSenha = estadoAtual.confirmarNovaSenha,
            senhaAtualCorreta = estadoAtual.senhaAtual == usuarioAutenticadoAtual.senha,
        )

        if (resultadoValidacao.temErro) {
            estado = estadoAtual.copy(
                erroNomeCompleto = resultadoValidacao.erroNomeCompleto,
                erroEmail = resultadoValidacao.erroEmail,
                erroSenhaAtual = resultadoValidacao.erroSenhaAtual,
                erroNovaSenha = resultadoValidacao.erroNovaSenha,
                erroConfirmarNovaSenha = resultadoValidacao.erroConfirmarNovaSenha,
                mensagemErro = null,
                mensagemSucesso = null,
            )
            return
        }

        val novaSenhaFinal = estadoAtual.novaSenha.takeIf { it.isNotBlank() }

        viewModelScope.launch(Dispatchers.IO) {
            when (
                val resultado = servicoPerfil.atualizarPerfil(
                    usuarioAtual = usuarioAutenticadoAtual,
                    nomeCompleto = estadoAtual.nomeCompleto,
                    email = estadoAtual.email,
                    novaSenha = novaSenhaFinal,
                )
            ) {
                is ResultadoAtualizacaoPerfilLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            mensagemErro = resultado.mensagem,
                            mensagemSucesso = null,
                        )
                    }
                }

                is ResultadoAtualizacaoPerfilLocal.Sucesso -> {
                    withContext(Dispatchers.Main) {
                        usuarioAutenticadoAtual = resultado.usuario
                        estado = estado.copy(
                            nomeCompleto = resultado.usuario.nome,
                            email = resultado.usuario.email,
                            senhaAtual = "",
                            novaSenha = "",
                            confirmarNovaSenha = "",
                            erroNomeCompleto = null,
                            erroEmail = null,
                            erroSenhaAtual = null,
                            erroNovaSenha = null,
                            erroConfirmarNovaSenha = null,
                            mensagemErro = null,
                            mensagemSucesso = "Alterações salvas com sucesso!",
                        )
                        aoPerfilAtualizado(resultado.usuario)
                    }
                }
            }
        }
    }
}

class PerfilViewModelFactory(
    private val context: Context,
    private val usuarioAutenticado: Usuario,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            val armazenamento = ArmazenamentoUsuariosArquivo(context.applicationContext)
            val repositorio = RepositorioUsuarioLocal(armazenamento)
            val servico = ServicoPerfilLocal(repositorio)
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(servico, usuarioAutenticado) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
