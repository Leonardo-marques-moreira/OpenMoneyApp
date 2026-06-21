package com.openmoney.app.ui.conta

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.conta.CalculadoraSaldoConta
import com.openmoney.app.domain.model.Conta
import com.openmoney.app.domain.model.TipoConta
import com.openmoney.app.domain.model.Usuario
import com.openmoney.app.ui.principal.BotaoAcaoCabecalho
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTheme
import java.math.BigDecimal
import java.time.LocalDate

private val CorCartaoConta = Color(0xFF231F1F)

@Composable
fun TelaMinhasContas(
    usuario: Usuario,
    contas: List<Conta>,
    carregando: Boolean,
    mensagemSucesso: String?,
    aoClicarMenu: () -> Unit,
    aoClicarNovaConta: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val saldoTotal = CalculadoraSaldoConta.calcularSaldoTotal(contas)
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Minhas contas",
                aoClicarAcaoEsquerda = aoClicarMenu,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.MENU,
                conteudoDireita = {
                    BotaoAcaoCabecalho(
                        texto = "+ Nova",
                        aoClicar = aoClicarNovaConta,
                    )
                },
                conteudoInferior = {
                    Text(
                        text = "Saldo total: ${formatarMoedaOpenMoney(saldoTotal)}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = usuario.nome,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f),
                        ),
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
            ) {
                if (mensagemSucesso != null) {
                    Text(
                        text = mensagemSucesso,
                        color = OpenMoneyGreen,
                        style = TextStyle(fontSize = 14.sp),
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }

                when {
                    carregando -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = OpenMoneyGreen)
                        }
                    }

                    contas.isEmpty() -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = CorCartaoConta,
                        ) {
                            Text(
                                text = "Nenhuma conta cadastrada ate o momento.",
                                modifier = Modifier.padding(20.dp),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                ),
                            )
                        }
                    }

                    else -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                        ) {
                            contas.forEach { conta ->
                                CartaoContaResumo(conta = conta)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartaoContaResumo(conta: Conta) {
    val saldoExibido = CalculadoraSaldoConta.calcularSaldoExibido(conta)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = CorCartaoConta,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 52.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp),
                        ),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = conta.nome
                            .replace(" ", "")
                            .take(2)
                            .uppercase(),
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = conta.cor.toComposeColorConta(),
                        ),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = conta.nome,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = conta.tipo.descricao,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary,
                        ),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = formatarMoedaOpenMoney(saldoExibido),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = formatarCorSaldoOpenMoney(saldoExibido),
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2B2B)
@Composable
private fun PreviaTelaMinhasContas() {
    OpenMoneyTheme {
        TelaMinhasContas(
            usuario = Usuario(
                id = 1L,
                nome = "Joao",
                email = "joao@openmoney.com",
                senha = "Senha123",
                dataCadastro = LocalDate.now(),
            ),
            contas = listOf(
                Conta(
                    id = 1L,
                    nome = "Nubank",
                    tipo = TipoConta.POUPANCA,
                    cor = "#7A3FE0",
                    saldo = BigDecimal("100.00"),
                    usuarioId = 1L,
                ),
                Conta(
                    id = 2L,
                    nome = "Santander",
                    tipo = TipoConta.CARTAO_CREDITO,
                    cor = "#C73633",
                    saldo = BigDecimal("10000.00"),
                    usuarioId = 1L,
                ),
            ),
            carregando = false,
            mensagemSucesso = null,
            aoClicarMenu = {},
            aoClicarNovaConta = {},
        )
    }
}
