package com.openmoney.app.ui.principal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.TipoTransacao
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.conta.formatarMoedaOpenMoney
import com.openmoney.app.ui.conta.toComposeColorConta
import com.openmoney.app.ui.painel.EstadoTelaDashboard
import com.openmoney.app.ui.painel.ItemUltimaTransacaoDashboard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

private val VerdeOpenMoney = Color(0xFF1D9E75)
private val FundoEscuroOpenMoney = Color(0xFF2A2A2A)
private val TextoClaroOpenMoney = Color(0xFFD9D9D9)
private val VerdeReceitaOpenMoney = Color(0xFF3B6D11)
private val VermelhoDespesaOpenMoney = Color(0xFFA32D2D)
private val BordaCardOpenMoney = Color(0xFFD9D9D9)
private val FundoCardTransacaoOpenMoney = Color(0xFF1F1C1C)

@Composable
fun TelaDashboard(
    usuario: Usuario,
    estadoPainel: EstadoTelaDashboard,
    aoClicarSair: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nomeExibicao = remember(usuario.nome) {
        usuario.nome.substringBefore(" ").ifBlank { usuario.nome }
    }
    val competenciaAtual = remember {
        formatarCompetenciaAtual(LocalDate.now())
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FundoEscuroOpenMoney),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            color = VerdeOpenMoney,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 34.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column {
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = CircleShape,
                            color = TextoClaroOpenMoney,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = nomeExibicao.take(1).uppercase(),
                                    style = TextStyle(
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VerdeOpenMoney,
                                    ),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Ola, $nomeExibicao",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = TextoClaroOpenMoney,
                            ),
                        )
                        Text(
                            text = competenciaAtual,
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = TextoClaroOpenMoney,
                            ),
                        )
                    }

                    Button(
                        onClick = aoClicarSair,
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TextoClaroOpenMoney,
                            contentColor = VerdeOpenMoney,
                        ),
                    ) {
                        Text(
                            text = "Sair",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Saldo total",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoClaroOpenMoney,
                    ),
                )
                Text(
                    text = formatarMoedaOpenMoney(estadoPainel.saldoTotal),
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = TextoClaroOpenMoney,
                    ),
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(217.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                CartaoResumoFinanceiro(
                    titulo = "Receitas",
                    valor = formatarMoedaOpenMoney(estadoPainel.totalReceitas),
                    corValor = VerdeReceitaOpenMoney,
                    modifier = Modifier.weight(1f),
                )
                CartaoResumoFinanceiro(
                    titulo = "Despesas",
                    valor = formatarMoedaOpenMoney(estadoPainel.totalDespesas),
                    corValor = VermelhoDespesaOpenMoney,
                    modifier = Modifier.weight(1f),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 40.dp),
            ) {
                Text(
                    text = "Ultimas transacoes",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = TextoClaroOpenMoney,
                    ),
                )

                Spacer(modifier = Modifier.height(20.dp))

                when {
                    estadoPainel.carregando -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = VerdeOpenMoney)
                        }
                    }

                    estadoPainel.ultimasTransacoes.isEmpty() -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            color = FundoCardTransacaoOpenMoney,
                        ) {
                            Text(
                                text = "Nenhuma transacao lancada ainda. Assim que voce registrar movimentacoes, elas aparecem aqui.",
                                modifier = Modifier.padding(20.dp),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = TextoClaroOpenMoney,
                                    textAlign = TextAlign.Start,
                                ),
                            )
                        }
                    }

                    else -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            estadoPainel.ultimasTransacoes.forEach { transacao ->
                                CartaoUltimaTransacao(transacao = transacao)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartaoResumoFinanceiro(
    titulo: String,
    valor: String,
    corValor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(15.dp),
        color = FundoEscuroOpenMoney,
        border = BorderStroke(
            width = 1.dp,
            color = BordaCardOpenMoney,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = titulo,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = TextoClaroOpenMoney,
                ),
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valor,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = corValor,
                ),
            )
        }
    }
}

@Composable
private fun CartaoUltimaTransacao(
    transacao: ItemUltimaTransacaoDashboard,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = FundoCardTransacaoOpenMoney,
        border = BorderStroke(1.dp, BordaCardOpenMoney.copy(alpha = 0.15f)),
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
                color = TextoClaroOpenMoney,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = resolverIconeCategoria(transacao.codigoIconeCategoria),
                        contentDescription = transacao.categoriaNome,
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
                        color = TextoClaroOpenMoney,
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatarDataDashboard(transacao.data)} - ${transacao.categoriaNome}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = TextoClaroOpenMoney.copy(alpha = 0.85f),
                    ),
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = formatarValorTransacao(transacao),
                textAlign = TextAlign.End,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (transacao.tipo == TipoTransacao.RECEITA) {
                        VerdeReceitaOpenMoney
                    } else {
                        VermelhoDespesaOpenMoney
                    },
                ),
            )
        }
    }
}

private fun formatarCompetenciaAtual(data: LocalDate): String {
    val localidadeBrasil = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()
    val mes = data.month.getDisplayName(JavaTextStyle.FULL, localidadeBrasil).lowercase()
    val anoCurto = data.year.toString().takeLast(2)
    return "$mes $anoCurto"
}

private fun formatarDataDashboard(data: LocalDate): String {
    return data.format(DateTimeFormatter.ofPattern("dd/MM"))
}

private fun formatarValorTransacao(transacao: ItemUltimaTransacaoDashboard): String {
    val prefixo = if (transacao.tipo == TipoTransacao.RECEITA) "+ " else "- "
    return prefixo + formatarMoedaOpenMoney(transacao.valor)
}

private fun resolverIconeCategoria(codigo: String): ImageVector {
    return when (codigo) {
        "shopping_cart" -> Icons.Outlined.ShoppingCart
        "home" -> Icons.Outlined.Home
        "pets" -> Icons.Outlined.Pets
        "restaurant" -> Icons.Outlined.Restaurant
        "school" -> Icons.Outlined.School
        "healing" -> Icons.Outlined.Favorite
        "directions_bus" -> Icons.Outlined.DirectionsBus
        "flight" -> Icons.Outlined.Flight
        "sports_esports" -> Icons.Outlined.SportsEsports
        else -> Icons.Outlined.AccountBalanceWallet
    }
}
