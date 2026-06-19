package com.openmoney.app.ui.relatorio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.ui.conta.formatarMoedaOpenMoney
import com.openmoney.app.ui.conta.toComposeColorConta
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen

private val FundoRelatorioOpenMoney = Color(0xFF2A2A2A)
private val FundoCardRelatorioOpenMoney = Color(0xFF231F1F)
private val VerdeReceitaRelatorio = Color(0xFF3B6D11)
private val VermelhoDespesaRelatorio = Color(0xFFA32D2D)
private val TextoClaroRelatorio = Color(0xFFD9D9D9)

@Composable
fun TelaRelatorios(
    estado: EstadoTelaRelatorios,
    aoVoltarInicio: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = FundoRelatorioOpenMoney,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Relatorios",
                subtitulo = "Resumo das suas movimentacoes",
                aoClicarAcaoEsquerda = aoVoltarInicio,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
                conteudoInferior = {
                    Text(
                        text = "Saldo atual: ${formatarMoedaOpenMoney(estado.saldoAtual)}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                },
            )

            if (estado.carregando) {
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
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CartaoResumoRelatorio(
                            titulo = "Receitas",
                            valor = formatarMoedaOpenMoney(estado.totalReceitas),
                            corValor = VerdeReceitaRelatorio,
                            modifier = Modifier.weight(1f),
                        )
                        CartaoResumoRelatorio(
                            titulo = "Despesas",
                            valor = formatarMoedaOpenMoney(estado.totalDespesas),
                            corValor = VermelhoDespesaRelatorio,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    CartaoResumoRelatorio(
                        titulo = "Saldo movimentado",
                        valor = formatarMoedaOpenMoney(estado.saldoMovimentado),
                        corValor = if (estado.saldoMovimentado.signum() >= 0) {
                            VerdeReceitaRelatorio
                        } else {
                            VermelhoDespesaRelatorio
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    SecaoRelatorioCategoria(
                        titulo = "Receitas por categoria",
                        itens = estado.receitasPorCategoria,
                        corPadraoValor = VerdeReceitaRelatorio,
                        mensagemVazia = "Nenhuma receita registrada ate o momento.",
                    )

                    SecaoRelatorioCategoria(
                        titulo = "Despesas por categoria",
                        itens = estado.despesasPorCategoria,
                        corPadraoValor = VermelhoDespesaRelatorio,
                        mensagemVazia = "Nenhuma despesa registrada ate o momento.",
                    )
                }
            }
        }
    }
}

@Composable
private fun CartaoResumoRelatorio(
    titulo: String,
    valor: String,
    corValor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = FundoCardRelatorioOpenMoney,
        border = BorderStroke(1.dp, TextoClaroRelatorio.copy(alpha = 0.15f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
        ) {
            Text(
                text = titulo,
                style = TextStyle(
                    fontSize = 17.sp,
                    color = TextoClaroRelatorio,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = corValor,
                ),
            )
        }
    }
}

@Composable
private fun SecaoRelatorioCategoria(
    titulo: String,
    itens: List<ItemResumoCategoriaRelatorio>,
    corPadraoValor: Color,
    mensagemVazia: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = titulo,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = TextoClaroRelatorio,
            ),
        )

        if (itens.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = FundoCardRelatorioOpenMoney,
            ) {
                Text(
                    text = mensagemVazia,
                    modifier = Modifier.padding(18.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = TextoClaroRelatorio.copy(alpha = 0.85f),
                    ),
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itens.forEach { item ->
                    LinhaResumoCategoriaRelatorio(
                        item = item,
                        corPadraoValor = corPadraoValor,
                    )
                }
            }
        }
    }
}

@Composable
private fun LinhaResumoCategoriaRelatorio(
    item: ItemResumoCategoriaRelatorio,
    corPadraoValor: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = FundoCardRelatorioOpenMoney,
        border = BorderStroke(1.dp, TextoClaroRelatorio.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(
                        color = item.corCategoria.toComposeColorConta(),
                        shape = CircleShape,
                    ),
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = item.categoriaNome,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = TextoClaroRelatorio,
                ),
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = formatarMoedaOpenMoney(item.total),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = corPadraoValor,
                ),
            )
        }
    }
}
