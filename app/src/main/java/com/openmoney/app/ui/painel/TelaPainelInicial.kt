package com.openmoney.app.ui.painel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.conta.EstadoTelaConta
import com.openmoney.app.ui.conta.TelaMinhasContas

@Composable
fun TelaPainelInicial(
    usuario: Usuario,
    estadoConta: EstadoTelaConta,
    aoClicarMenu: () -> Unit,
    aoClicarNovaConta: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TelaMinhasContas(
        usuario = usuario,
        contas = estadoConta.contas,
        carregando = estadoConta.carregando,
        mensagemSucesso = estadoConta.mensagemSucesso,
        aoClicarMenu = aoClicarMenu,
        aoClicarNovaConta = aoClicarNovaConta,
        modifier = modifier,
    )
}
