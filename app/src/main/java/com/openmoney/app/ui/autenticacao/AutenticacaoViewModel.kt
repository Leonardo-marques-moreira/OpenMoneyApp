package com.openmoney.app.ui.autenticacao

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openmoney.app.data.local.armazenamento.ArmazenamentoUsuariosArquivo
import com.openmoney.app.data.repositorio.RepositorioUsuarioLocal
import com.openmoney.app.domain.autenticacao.ResultadoAutenticacaoLocal
import com.openmoney.app.domain.autenticacao.ServicoAutenticacaoLocal
import com.openmoney.app.domain.autenticacao.ValidadorCamposLogin
import com.openmoney.app.domain.cadastro.ResultadoCadastroLocal
import com.openmoney.app.domain.cadastro.ValidadorCadastroUsuario
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.cadastro.EstadoTelaCadastro
import com.openmoney.app.ui.login.EstadoTelaLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AutenticacaoViewModel(
    private val servicoAutenticacao: ServicoAutenticacaoLocal,
) : ViewModel() {

    var estado by mutableStateOf(EstadoAutenticacaoApp())
        private set

    fun atualizarEmailLogin(novoEmail: String) {
        estado = estado.copy(
            estadoLogin = estado.estadoLogin.copy(
                email = novoEmail,
                erroEmail = null,
                mensagemAutenticacao = null,
                mensagemSucesso = null,
            )
        )
    }

    fun atualizarSenhaLogin(novaSenha: String) {
        estado = estado.copy(
            estadoLogin = estado.estadoLogin.copy(
                senha = novaSenha,
                erroSenha = null,
                mensagemAutenticacao = null,
                mensagemSucesso = null,
            )
        )
    }

    fun alternarVisibilidadeSenhaLogin() {
        estado = estado.copy(
            estadoLogin = estado.estadoLogin.copy(
                senhaVisivel = !estado.estadoLogin.senhaVisivel,
            )
        )
    }

    fun entrar() {
        val loginAtual = estado.estadoLogin
        val resultadoValidacao = ValidadorCamposLogin.validar(
            email = loginAtual.email,
            senha = loginAtual.senha,
        )

        if (resultadoValidacao.temErro) {
            estado = estado.copy(
                estadoLogin = loginAtual.copy(
                    erroEmail = resultadoValidacao.erroEmail,
                    erroSenha = resultadoValidacao.erroSenha,
                    mensagemAutenticacao = null,
                    mensagemSucesso = null,
                )
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (val resultado = servicoAutenticacao.autenticar(loginAtual.email, loginAtual.senha)) {
                is ResultadoAutenticacaoLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            estadoLogin = estado.estadoLogin.copy(
                                mensagemAutenticacao = resultado.mensagem,
                                mensagemSucesso = null,
                            )
                        )
                    }
                }

                is ResultadoAutenticacaoLocal.Sucesso -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            destinoAtual = DestinoAutenticacao.PAINEL,
                            usuarioAutenticado = resultado.usuario,
                            estadoLogin = estado.estadoLogin.copy(
                                senha = "",
                                mensagemAutenticacao = null,
                            ),
                        )
                    }
                }
            }
        }
    }

    fun irParaCadastro() {
        estado = estado.copy(destinoAtual = DestinoAutenticacao.CADASTRO)
    }

    fun irParaLogin() {
        estado = estado.copy(
            destinoAtual = DestinoAutenticacao.LOGIN,
            estadoCadastro = estado.estadoCadastro.copy(mensagemCadastro = null),
        )
    }

    fun atualizarNomeCadastro(novoNome: String) {
        estado = estado.copy(
            estadoCadastro = estado.estadoCadastro.copy(
                nome = novoNome,
                erroNome = null,
                mensagemCadastro = null,
            )
        )
    }

    fun atualizarEmailCadastro(novoEmail: String) {
        estado = estado.copy(
            estadoCadastro = estado.estadoCadastro.copy(
                email = novoEmail,
                erroEmail = null,
                mensagemCadastro = null,
            )
        )
    }

    fun atualizarSenhaCadastro(novaSenha: String) {
        estado = estado.copy(
            estadoCadastro = estado.estadoCadastro.copy(
                senha = novaSenha,
                erroSenha = null,
                erroConfirmarSenha = null,
                mensagemCadastro = null,
            )
        )
    }

    fun atualizarConfirmacaoSenhaCadastro(novaConfirmacao: String) {
        estado = estado.copy(
            estadoCadastro = estado.estadoCadastro.copy(
                confirmarSenha = novaConfirmacao,
                erroConfirmarSenha = null,
                mensagemCadastro = null,
            )
        )
    }

    fun alternarVisibilidadeSenhaCadastro() {
        estado = estado.copy(
            estadoCadastro = estado.estadoCadastro.copy(
                senhaVisivel = !estado.estadoCadastro.senhaVisivel,
            )
        )
    }

    fun alternarVisibilidadeConfirmacaoSenhaCadastro() {
        estado = estado.copy(
            estadoCadastro = estado.estadoCadastro.copy(
                confirmarSenhaVisivel = !estado.estadoCadastro.confirmarSenhaVisivel,
            )
        )
    }

    fun cadastrar() {
        val cadastroAtual = estado.estadoCadastro
        val resultadoValidacao = ValidadorCadastroUsuario.validar(
            nome = cadastroAtual.nome,
            email = cadastroAtual.email,
            senha = cadastroAtual.senha,
            confirmarSenha = cadastroAtual.confirmarSenha,
        )

        if (resultadoValidacao.temErro) {
            estado = estado.copy(
                estadoCadastro = cadastroAtual.copy(
                    erroNome = resultadoValidacao.erroNome,
                    erroEmail = resultadoValidacao.erroEmail,
                    erroSenha = resultadoValidacao.erroSenha,
                    erroConfirmarSenha = resultadoValidacao.erroConfirmarSenha,
                    mensagemCadastro = null,
                )
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (val resultado = servicoAutenticacao.cadastrar(cadastroAtual.nome, cadastroAtual.email, cadastroAtual.senha)) {
                is ResultadoCadastroLocal.Erro -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            estadoCadastro = estado.estadoCadastro.copy(
                                mensagemCadastro = resultado.mensagem,
                            )
                        )
                    }
                }

                is ResultadoCadastroLocal.Sucesso -> {
                    withContext(Dispatchers.Main) {
                        estado = estado.copy(
                            destinoAtual = DestinoAutenticacao.LOGIN,
                            estadoCadastro = EstadoTelaCadastro(),
                            estadoLogin = EstadoTelaLogin(
                                email = resultado.usuario.email,
                                mensagemSucesso = "Registro gravado com sucesso!",
                            ),
                        )
                    }
                }
            }
        }
    }

    fun sair() {
        estado = estado.copy(
            destinoAtual = DestinoAutenticacao.LOGIN,
            usuarioAutenticado = null,
            estadoLogin = EstadoTelaLogin(),
        )
    }

    fun atualizarUsuarioAutenticado(usuarioAtualizado: Usuario) {
        if (estado.usuarioAutenticado?.id != usuarioAtualizado.id) {
            return
        }

        estado = estado.copy(usuarioAutenticado = usuarioAtualizado)
    }
}

class AutenticacaoViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AutenticacaoViewModel::class.java)) {
            val armazenamento = ArmazenamentoUsuariosArquivo(context.applicationContext)
            val repositorio = RepositorioUsuarioLocal(armazenamento)
            val servico = ServicoAutenticacaoLocal(repositorio)
            @Suppress("UNCHECKED_CAST")
            return AutenticacaoViewModel(servico) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
