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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.ui.categoria.resolverIconeCategoriaDisponivel
import com.openmoney.app.ui.conta.formatarMoedaOpenMoney
import com.openmoney.app.ui.conta.toComposeColorConta
import com.openmoney.app.ui.principal.BotaoAcaoCabecalho
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTheme
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val FundoCartaoTransacao = Color(0xFF1F1C1C)
private val CorTextoClaro = Color(0xFFD9D9D9)
private val CorReceita = Color(0xFF3B6D11)
private val CorDespesa = Color(0xFFA32D2D)

@Composable
fun TelaTransacoesCadastradas(
    transacoes: List<ItemTransacaoCadastrada>,
    carregando: Boolean,
    mensagemErro: String?,
    aoClicarVoltar: () -> Unit,
    aoClicarNovaTransacao: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Transações cadastradas",
                subtitulo = "Receitas e despesas registradas",
                aoClicarAcaoEsquerda = aoClicarVoltar,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
                conteudoDireita = {
                    BotaoAcaoCabecalho(
                        texto = "+ Nova",
                        aoClicar = aoClicarNovaTransacao,
                    )
                },
            )

            when {
                carregando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = OpenMoneyGreen)
                    }
                }

                !mensagemErro.isNullOrBlank() -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        shape = RoundedCornerShape(22.dp),
                        color = FundoCartaoTransacao,
                    ) {
                        Text(
                            text = mensagemErro,
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.error,
                            ),
                        )
                    }
                }

                transacoes.isEmpty() -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        shape = RoundedCornerShape(22.dp),
                        color = FundoCartaoTransacao,
                    ) {
                        Text(
                            text = "Nenhuma transação cadastrada até o momento.",
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.secondary,
                            ),
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 20.dp,
                            vertical = 24.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            Text(
                                text = "Lançamentos registrados: ${transacoes.size}",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                ),
                            )
                        }

                        items(
                            items = transacoes,
                            key = { transacao -> transacao.id },
                        ) { transacao ->
                            CartaoTransacaoCadastrada(transacao = transacao)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartaoTransacaoCadastrada(
    transacao: ItemTransacaoCadastrada,
) {
    val iconeCategoria = resolverIconeCategoriaDisponivel(transacao.codigoIconeCategoria)
    val corValor = if (transacao.tipo == TipoTransacao.RECEITA) CorReceita else CorDespesa

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = FundoCartaoTransacao,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = CircleShape,
                color = CorTextoClaro,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = iconeCategoria.imagem,
                        contentDescription = transacao.nomeCategoria,
                        tint = transacao.corCategoria.toComposeColorConta(),
                        modifier = Modifier.size(28.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = transacao.descricao,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = CorTextoClaro,
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${formatarDataTransacaoCadastrada(transacao.data)} - ${transacao.nomeCategoria}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = CorTextoClaro.copy(alpha = 0.85f),
                    ),
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = transacao.nomeConta,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = formatarValorTransacaoCadastrada(transacao),
                textAlign = TextAlign.End,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = corValor,
                ),
            )
        }
    }
}

private fun formatarDataTransacaoCadastrada(data: LocalDate): String {
    return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

private fun formatarValorTransacaoCadastrada(transacao: ItemTransacaoCadastrada): String {
    val prefixo = if (transacao.tipo == TipoTransacao.RECEITA) "+ " else "- "
    return prefixo + formatarMoedaOpenMoney(transacao.valor)
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaTransacoesCadastradas() {
    OpenMoneyTheme {
        TelaTransacoesCadastradas(
            transacoes = listOf(
                ItemTransacaoCadastrada(
                    id = 1L,
                    descricao = "Salário",
                    valor = BigDecimal("6500.00"),
                    tipo = TipoTransacao.RECEITA,
                    data = LocalDate.of(2026, 3, 20),
                    nomeConta = "Conta principal",
                    nomeCategoria = "Trabalho",
                    codigoIconeCategoria = "wallet",
                    corCategoria = "#25A67C",
                ),
                ItemTransacaoCadastrada(
                    id = 2L,
                    descricao = "Supermercado",
                    valor = BigDecimal("430.00"),
                    tipo = TipoTransacao.DESPESA,
                    data = LocalDate.of(2026, 3, 18),
                    nomeConta = "Nubank",
                    nomeCategoria = "Alimentação",
                    codigoIconeCategoria = "shopping_cart",
                    corCategoria = "#F4BB17",
                ),
            ),
            carregando = false,
            mensagemErro = null,
            aoClicarVoltar = {},
            aoClicarNovaTransacao = {},
        )
    }
}
