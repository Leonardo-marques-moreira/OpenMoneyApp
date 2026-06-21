package com.openmoney.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openmoney.app.ui.autenticacao.AutenticacaoViewModel
import com.openmoney.app.ui.autenticacao.AutenticacaoViewModelFactory
import com.openmoney.app.ui.autenticacao.DestinoAutenticacao
import com.openmoney.app.ui.cadastro.TelaCadastro
import com.openmoney.app.ui.login.TelaLogin
import com.openmoney.app.ui.principal.AreaAutenticadaOpenMoney
import com.openmoney.app.ui.principal.TelaMensagemSucesso

@Composable
fun OpenMoneyApp() {
    val context = LocalContext.current
    val fabricaAutenticacao = remember(context) {
        AutenticacaoViewModelFactory(context.applicationContext)
    }
    val autenticacaoViewModel: AutenticacaoViewModel = viewModel(factory = fabricaAutenticacao)
    val estado = autenticacaoViewModel.estado

    when (estado.destinoAtual) {
        DestinoAutenticacao.LOGIN -> {
            val mensagemSucesso = estado.estadoLogin.mensagemSucesso

            Box(modifier = Modifier.fillMaxSize()) {
                TelaLogin(
                    email = estado.estadoLogin.email,
                    senha = estado.estadoLogin.senha,
                    senhaVisivel = estado.estadoLogin.senhaVisivel,
                    erroEmail = estado.estadoLogin.erroEmail,
                    erroSenha = estado.estadoLogin.erroSenha,
                    mensagemAutenticacao = estado.estadoLogin.mensagemAutenticacao,
                    mensagemSucesso = estado.estadoLogin.mensagemSucesso,
                    aoAlterarEmail = autenticacaoViewModel::atualizarEmailLogin,
                    aoAlterarSenha = autenticacaoViewModel::atualizarSenhaLogin,
                    aoAlternarVisibilidadeSenha = autenticacaoViewModel::alternarVisibilidadeSenhaLogin,
                    aoClicarEntrar = autenticacaoViewModel::entrar,
                    aoClicarEsqueciSenha = {},
                    aoClicarCadastrar = autenticacaoViewModel::irParaCadastro,
                    modifier = Modifier.then(
                        if (!mensagemSucesso.isNullOrBlank()) {
                            Modifier.blur(16.dp)
                        } else {
                            Modifier
                        },
                    ),
                )

                if (!mensagemSucesso.isNullOrBlank()) {
                    TelaMensagemSucesso(
                        mensagem = mensagemSucesso,
                        textoBotao = "Fazer login",
                        aoClicarBotao = autenticacaoViewModel::confirmarSucessoCadastro,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        DestinoAutenticacao.CADASTRO -> {
            TelaCadastro(
                nome = estado.estadoCadastro.nome,
                email = estado.estadoCadastro.email,
                senha = estado.estadoCadastro.senha,
                confirmarSenha = estado.estadoCadastro.confirmarSenha,
                senhaVisivel = estado.estadoCadastro.senhaVisivel,
                confirmarSenhaVisivel = estado.estadoCadastro.confirmarSenhaVisivel,
                erroNome = estado.estadoCadastro.erroNome,
                erroEmail = estado.estadoCadastro.erroEmail,
                erroSenha = estado.estadoCadastro.erroSenha,
                erroConfirmarSenha = estado.estadoCadastro.erroConfirmarSenha,
                mensagemCadastro = estado.estadoCadastro.mensagemCadastro,
                aoAlterarNome = autenticacaoViewModel::atualizarNomeCadastro,
                aoAlterarEmail = autenticacaoViewModel::atualizarEmailCadastro,
                aoAlterarSenha = autenticacaoViewModel::atualizarSenhaCadastro,
                aoAlterarConfirmarSenha = autenticacaoViewModel::atualizarConfirmacaoSenhaCadastro,
                aoAlternarVisibilidadeSenha = autenticacaoViewModel::alternarVisibilidadeSenhaCadastro,
                aoAlternarVisibilidadeConfirmarSenha = autenticacaoViewModel::alternarVisibilidadeConfirmacaoSenhaCadastro,
                aoClicarCadastrar = autenticacaoViewModel::cadastrar,
                aoClicarEntrar = autenticacaoViewModel::irParaLogin,
            )
        }

        DestinoAutenticacao.PAINEL -> {
            estado.usuarioAutenticado?.let { usuario ->
                AreaAutenticadaOpenMoney(
                    usuario = usuario,
                    aoClicarSair = autenticacaoViewModel::sair,
                    aoAtualizarUsuarioAutenticado = autenticacaoViewModel::atualizarUsuarioAutenticado,
                )
            }
        }
    }
}
