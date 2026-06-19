package com.openmoney.app.ui.categoria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.ui.autenticacao.CampoAutenticacao
import com.openmoney.app.ui.autenticacao.RotuloCampoAutenticacao
import com.openmoney.app.ui.conta.CoresContaDisponiveis
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNovaCategoria(
    tipoSelecionado: TipoTransacao,
    nomeCategoria: String,
    iconeSelecionado: String,
    corSelecionada: String,
    erroNomeCategoria: String?,
    erroTipoCategoria: String?,
    mensagemErro: String?,
    mensagemSucesso: String?,
    aoSelecionarTipo: (TipoTransacao) -> Unit,
    aoAlterarNomeCategoria: (String) -> Unit,
    aoSelecionarIcone: (String) -> Unit,
    aoSelecionarCor: (String) -> Unit,
    aoClicarCancelar: () -> Unit,
    aoClicarSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuIconesExpandido by remember { mutableStateOf(false) }
    val iconeAtual = IconesCategoriaDisponiveis.firstOrNull { it.codigo == iconeSelecionado }
        ?: IconesCategoriaDisponiveis.first()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Nova categoria",
                aoClicarAcaoEsquerda = aoClicarCancelar,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 28.dp),
            ) {
                RotuloCampoAutenticacao(texto = "Tipo")
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    BotaoTipoCategoria(
                        texto = "Receita",
                        selecionado = tipoSelecionado == TipoTransacao.RECEITA,
                        aoSelecionar = { aoSelecionarTipo(TipoTransacao.RECEITA) },
                        modifier = Modifier.weight(1f),
                    )
                    BotaoTipoCategoria(
                        texto = "Despesa",
                        selecionado = tipoSelecionado == TipoTransacao.DESPESA,
                        aoSelecionar = { aoSelecionarTipo(TipoTransacao.DESPESA) },
                        modifier = Modifier.weight(1f),
                    )
                }

                if (erroTipoCategoria != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = erroTipoCategoria,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 13.sp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                RotuloCampoAutenticacao(texto = "Nome da categoria")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = nomeCategoria,
                    aoAlterarValor = aoAlterarNomeCategoria,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroNomeCategoria,
                )

                Spacer(modifier = Modifier.height(24.dp))

                RotuloCampoAutenticacao(texto = "Icone")
                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = menuIconesExpandido,
                    onExpandedChange = { menuIconesExpandido = !menuIconesExpandido },
                ) {
                    OutlinedTextField(
                        value = iconeAtual.descricao,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true,
                            ),
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuIconesExpandido)
                        },
                    )

                    ExposedDropdownMenu(
                        expanded = menuIconesExpandido,
                        onDismissRequest = { menuIconesExpandido = false },
                    ) {
                        IconesCategoriaDisponiveis.forEach { icone ->
                            DropdownMenuItem(
                                text = { Text(icone.descricao) },
                                onClick = {
                                    aoSelecionarIcone(icone.codigo)
                                    menuIconesExpandido = false
                                },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                RotuloCampoAutenticacao(texto = "Cor")
                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    CoresContaDisponiveis.forEach { cor ->
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Surface(
                                onClick = { aoSelecionarCor(cor) },
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = cor.toComposeColor(),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 2.dp,
                                    color = if (corSelecionada == cor) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                                ),
                            ) {}
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFD3F1B8),
                ) {
                    Text(
                        text = "A categoria ficara disponivel para os proximos lancamentos.",
                        modifier = Modifier.padding(18.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color(0xFF4A8F17),
                        ),
                    )
                }

                if (mensagemErro != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = mensagemErro,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }

                if (mensagemSucesso != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = mensagemSucesso,
                        color = OpenMoneyGreen,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

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
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.error,
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Text(text = "Cancelar", style = TextStyle(fontSize = 18.sp))
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
                        Text(text = "Salvar", style = TextStyle(fontSize = 18.sp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BotaoTipoCategoria(
    texto: String,
    selecionado: Boolean,
    aoSelecionar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val corFundo = if (selecionado) Color(0xFFD3F1B8) else Color.Transparent
    val corTexto = if (selecionado) Color(0xFF4A8F17) else MaterialTheme.colorScheme.onBackground
    val corBorda = if (selecionado) Color(0xFFD3F1B8) else MaterialTheme.colorScheme.outline

    OutlinedButton(
        onClick = aoSelecionar,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = corBorda,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = corFundo,
            contentColor = corTexto,
        ),
    ) {
        Text(text = texto, style = TextStyle(fontSize = 18.sp))
    }
}

private fun String.toComposeColor(): Color {
    return runCatching {
        Color(android.graphics.Color.parseColor(this))
    }.getOrElse {
        OpenMoneyGreen
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaNovaCategoria() {
    OpenMoneyTheme {
        TelaNovaCategoria(
            tipoSelecionado = TipoTransacao.RECEITA,
            nomeCategoria = "Pet",
            iconeSelecionado = "pets",
            corSelecionada = "#25A67C",
            erroNomeCategoria = null,
            erroTipoCategoria = null,
            mensagemErro = null,
            mensagemSucesso = null,
            aoSelecionarTipo = {},
            aoAlterarNomeCategoria = {},
            aoSelecionarIcone = {},
            aoSelecionarCor = {},
            aoClicarCancelar = {},
            aoClicarSalvar = {},
        )
    }
}
