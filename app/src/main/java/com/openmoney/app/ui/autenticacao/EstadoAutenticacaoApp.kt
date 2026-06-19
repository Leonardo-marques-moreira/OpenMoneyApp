package com.openmoney.app.ui.autenticacao

import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.cadastro.EstadoTelaCadastro
import com.openmoney.app.ui.login.EstadoTelaLogin

data class EstadoAutenticacaoApp(
    val destinoAtual: DestinoAutenticacao = DestinoAutenticacao.LOGIN,
    val estadoLogin: EstadoTelaLogin = EstadoTelaLogin(),
    val estadoCadastro: EstadoTelaCadastro = EstadoTelaCadastro(),
    val usuarioAutenticado: Usuario? = null,
)


