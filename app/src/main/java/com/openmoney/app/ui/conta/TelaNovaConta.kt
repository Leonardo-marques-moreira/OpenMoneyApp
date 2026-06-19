package com.openmoney.app.ui.conta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.TipoConta
import com.openmoney.app.ui.autenticacao.CampoAutenticacao
import com.openmoney.app.ui.autenticacao.RotuloCampoAutenticacao
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTheme

@Composable
fun TelaNovaConta(
    nomeConta: String,
    tipoContaSelecionado: TipoConta?,
    corSelecionada: String?,
    saldoInicial: String,
    erroNomeConta: String?,
    erroTipoConta: String?,
    erroSaldoInicial: String?,
    mensagemErro: String?,
    aoAlterarNomeConta: (String) -> Unit,
    aoSelecionarTipoConta: (TipoConta) -> Unit,
    aoSelecionarCor: (String) -> Unit,
    aoAlterarSaldoInicial: (String) -> Unit,
    aoClicarVoltar: () -> Unit,
    aoClicarCancelar: () -> Unit,
    aoClicarSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Nova conta",
                subtitulo = "Preencha os dados da conta",
                aoClicarAcaoEsquerda = aoClicarVoltar,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 26.dp),
            ) {
                RotuloCampoAutenticacao(texto = "Nome da conta")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = nomeConta,
                    aoAlterarValor = aoAlterarNomeConta,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroNomeConta,
                    placeholder = "Ex: Nubank, Carteira...",
                )

                Spacer(modifier = Modifier.height(34.dp))

                RotuloCampoAutenticacao(texto = "Tipo de conta")
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    TipoConta.entries.chunked(2).forEach { linhaTipos ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            linhaTipos.forEach { tipoConta ->
                                SeletorTipoConta(
                                    tipoConta = tipoConta,
                                    selecionado = tipoContaSelecionado == tipoConta,
                                    aoSelecionar = { aoSelecionarTipoConta(tipoConta) },
                                    modifier = Modifier.weight(1f),
                                )
                            }

                            if (linhaTipos.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                if (erroTipoConta != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = erroTipoConta,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 13.sp),
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                RotuloCampoAutenticacao(texto = "Saldo inicial")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = saldoInicial,
                    aoAlterarValor = aoAlterarSaldoInicial,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                    acoesTeclado = KeyboardActions(onDone = { aoClicarSalvar() }),
                    mensagemErro = erroSaldoInicial,
                    placeholder = "R$ 0,00",
                )

                if (erroSaldoInicial == null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Deve ser um valor maior ou igual a zero",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(38.dp))

                RotuloCampoAutenticacao(texto = "Cor de identificacao")
                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    CoresContaDisponiveis.forEach { cor ->
                        SeletorCorConta(
                            cor = cor,
                            selecionada = corSelecionada == cor,
                            aoSelecionar = { aoSelecionarCor(cor) },
                        )
                    }
                }

                if (mensagemErro != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = mensagemErro,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }

                Spacer(modifier = Modifier.height(42.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    OutlinedButton(
                        onClick = aoClicarCancelar,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.error,
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Text(
                            text = "Cancelar",
                            style = TextStyle(fontSize = 18.sp),
                        )
                    }

                    Button(
                        onClick = aoClicarSalvar,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OpenMoneyGreen,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Text(
                            text = "Salvar",
                            style = TextStyle(fontSize = 18.sp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeletorTipoConta(
    tipoConta: TipoConta,
    selecionado: Boolean,
    aoSelecionar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val corFundo = if (selecionado) Color(0xFFD3F1B8) else Color.Transparent
    val corTexto = if (selecionado) Color(0xFF4A8F17) else MaterialTheme.colorScheme.onBackground
    val corBorda = if (selecionado) Color(0xFFD3F1B8) else MaterialTheme.colorScheme.outline

    OutlinedButton(
        onClick = aoSelecionar,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = corBorda,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = corFundo,
            contentColor = corTexto,
        ),
    ) {
        Text(
            text = tipoConta.descricao,
            style = TextStyle(fontSize = 16.sp),
        )
    }
}

@Composable
private fun SeletorCorConta(
    cor: String,
    selecionada: Boolean,
    aoSelecionar: () -> Unit,
) {
    val corBorda = if (selecionada) MaterialTheme.colorScheme.onBackground else Color.Transparent

    Surface(
        onClick = aoSelecionar,
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = cor.toComposeColorConta(),
        border = BorderStroke(2.dp, corBorda),
    ) {}
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaNovaConta() {
    OpenMoneyTheme {
        TelaNovaConta(
            nomeConta = "",
            tipoContaSelecionado = TipoConta.CONTA_CORRENTE,
            corSelecionada = "#25A67C",
            saldoInicial = "",
            erroNomeConta = null,
            erroTipoConta = null,
            erroSaldoInicial = null,
            mensagemErro = null,
            aoAlterarNomeConta = {},
            aoSelecionarTipoConta = {},
            aoSelecionarCor = {},
            aoAlterarSaldoInicial = {},
            aoClicarVoltar = {},
            aoClicarCancelar = {},
            aoClicarSalvar = {},
        )
    }
}
