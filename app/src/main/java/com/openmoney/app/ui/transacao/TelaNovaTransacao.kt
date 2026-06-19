package com.openmoney.app.ui.transacao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoConta
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.ui.autenticacao.CampoAutenticacao
import com.openmoney.app.ui.autenticacao.RotuloCampoAutenticacao
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTheme
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNovaTransacao(
    tipoSelecionado: TipoTransacao,
    valor: String,
    descricao: String,
    dataLancamento: String,
    contaSelecionadaId: Long?,
    categoriaSelecionadaId: Long?,
    contasDisponiveis: List<Conta>,
    categoriasDisponiveis: List<Categoria>,
    carregando: Boolean,
    erroValor: String?,
    erroDescricao: String?,
    erroCategoria: String?,
    erroConta: String?,
    erroData: String?,
    mensagemErro: String?,
    mensagemSucesso: String?,
    aoSelecionarTipo: (TipoTransacao) -> Unit,
    aoAlterarValor: (String) -> Unit,
    aoAlterarDescricao: (String) -> Unit,
    aoAlterarData: (String) -> Unit,
    aoSelecionarCategoria: (Long) -> Unit,
    aoSelecionarConta: (Long) -> Unit,
    aoAbrirNovaCategoria: () -> Unit,
    aoClicarVoltar: () -> Unit,
    aoClicarSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuCategoriasExpandido by remember { mutableStateOf(false) }
    var menuContasExpandido by remember { mutableStateOf(false) }

    val categoriaSelecionada = categoriasDisponiveis.firstOrNull { it.id == categoriaSelecionadaId }
    val contaSelecionada = contasDisponiveis.firstOrNull { it.id == contaSelecionadaId }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Nova transacao",
                aoClicarAcaoEsquerda = aoClicarVoltar,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
            )

            if (carregando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = OpenMoneyGreen)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 28.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        BotaoTipoTransacao(
                            texto = "Receita",
                            selecionado = tipoSelecionado == TipoTransacao.RECEITA,
                            aoSelecionar = { aoSelecionarTipo(TipoTransacao.RECEITA) },
                            modifier = Modifier.weight(1f),
                        )
                        BotaoTipoTransacao(
                            texto = "Despesa",
                            selecionado = tipoSelecionado == TipoTransacao.DESPESA,
                            aoSelecionar = { aoSelecionarTipo(TipoTransacao.DESPESA) },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    RotuloCampoAutenticacao(texto = "Valor")
                    Spacer(modifier = Modifier.height(10.dp))
                    CampoAutenticacao(
                        valor = valor,
                        aoAlterarValor = aoAlterarValor,
                        opcoesTeclado = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                        ),
                        mensagemErro = erroValor,
                        placeholder = "R$ 0,00",
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RotuloCampoAutenticacao(texto = "Descricao")
                    Spacer(modifier = Modifier.height(10.dp))
                    CampoAutenticacao(
                        valor = descricao,
                        aoAlterarValor = aoAlterarDescricao,
                        opcoesTeclado = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                        mensagemErro = erroDescricao,
                        placeholder = "Ex: Salario, mercado...",
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RotuloCampoAutenticacao(texto = "Categoria")
                    Spacer(modifier = Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = menuCategoriasExpandido,
                        onExpandedChange = {
                            if (categoriasDisponiveis.isNotEmpty()) {
                                menuCategoriasExpandido = !menuCategoriasExpandido
                            }
                        },
                    ) {
                        OutlinedTextField(
                            value = categoriaSelecionada?.nome.orEmpty(),
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
                            isError = erroCategoria != null,
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                            placeholder = {
                                Text(
                                    text = "Selecionar...",
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCategoriasExpandido)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            ),
                        )

                        ExposedDropdownMenu(
                            expanded = menuCategoriasExpandido,
                            onDismissRequest = { menuCategoriasExpandido = false },
                        ) {
                            categoriasDisponiveis.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria.nome) },
                                    onClick = {
                                        aoSelecionarCategoria(categoria.id)
                                        menuCategoriasExpandido = false
                                    },
                                )
                            }
                        }
                    }

                    if (erroCategoria != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = erroCategoria,
                            color = MaterialTheme.colorScheme.error,
                            style = TextStyle(fontSize = 13.sp),
                        )
                    } else if (categoriasDisponiveis.isEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhuma categoria disponivel para o tipo selecionado.",
                            color = MaterialTheme.colorScheme.secondary,
                            style = TextStyle(fontSize = 14.sp),
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = aoAbrirNovaCategoria,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = OpenMoneyGreen,
                        ),
                    ) {
                        Text(text = "+ Nova categoria", style = TextStyle(fontSize = 16.sp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    RotuloCampoAutenticacao(texto = "Data")
                    Spacer(modifier = Modifier.height(10.dp))
                    CampoAutenticacao(
                        valor = dataLancamento,
                        aoAlterarValor = aoAlterarData,
                        opcoesTeclado = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        mensagemErro = erroData,
                        placeholder = "DD/MM/AAAA",
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RotuloCampoAutenticacao(texto = "Conta")
                    Spacer(modifier = Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = menuContasExpandido,
                        onExpandedChange = {
                            if (contasDisponiveis.isNotEmpty()) {
                                menuContasExpandido = !menuContasExpandido
                            }
                        },
                    ) {
                        OutlinedTextField(
                            value = contaSelecionada?.nome.orEmpty(),
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
                            isError = erroConta != null,
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                            placeholder = {
                                Text(
                                    text = "Selecionar...",
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuContasExpandido)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            ),
                        )

                        ExposedDropdownMenu(
                            expanded = menuContasExpandido,
                            onDismissRequest = { menuContasExpandido = false },
                        ) {
                            contasDisponiveis.forEach { conta ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(conta.nome)
                                            Text(
                                                text = conta.tipo.descricao,
                                                style = TextStyle(
                                                    fontSize = 13.sp,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                ),
                                            )
                                        }
                                    },
                                    onClick = {
                                        aoSelecionarConta(conta.id)
                                        menuContasExpandido = false
                                    },
                                )
                            }
                        }
                    }

                    if (erroConta != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = erroConta,
                            color = MaterialTheme.colorScheme.error,
                            style = TextStyle(fontSize = 13.sp),
                        )
                    } else if (contasDisponiveis.isEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cadastre ao menos uma conta para registrar transacoes.",
                            color = MaterialTheme.colorScheme.secondary,
                            style = TextStyle(fontSize = 14.sp),
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

                    Button(
                        onClick = aoClicarSalvar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OpenMoneyGreen,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Text(text = "Salvar", style = TextStyle(fontSize = 22.sp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BotaoTipoTransacao(
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
        border = BorderStroke(
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

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaNovaTransacao() {
    OpenMoneyTheme {
        TelaNovaTransacao(
            tipoSelecionado = TipoTransacao.RECEITA,
            valor = "",
            descricao = "",
            dataLancamento = "24/03/2026",
            contaSelecionadaId = 1L,
            categoriaSelecionadaId = 1L,
            contasDisponiveis = listOf(
                Conta(
                    id = 1L,
                    nome = "Conta principal",
                    tipo = TipoConta.CONTA_CORRENTE,
                    cor = "#25A67C",
                    saldo = BigDecimal("4280.50"),
                    usuarioId = 1L,
                ),
            ),
            categoriasDisponiveis = listOf(
                Categoria(
                    id = 1L,
                    nome = "Salario",
                    tipo = TipoTransacao.RECEITA,
                    icone = "wallet",
                    cor = "#25A67C",
                    usuarioId = 1L,
                ),
            ),
            carregando = false,
            erroValor = null,
            erroDescricao = null,
            erroCategoria = null,
            erroConta = null,
            erroData = null,
            mensagemErro = null,
            mensagemSucesso = null,
            aoSelecionarTipo = {},
            aoAlterarValor = {},
            aoAlterarDescricao = {},
            aoAlterarData = {},
            aoSelecionarCategoria = {},
            aoSelecionarConta = {},
            aoAbrirNovaCategoria = {},
            aoClicarVoltar = {},
            aoClicarSalvar = {},
        )
    }
}
