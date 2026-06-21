package com.openmoney.app.ui.relatorio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.ui.conta.formatarMoedaOpenMoney
import com.openmoney.app.ui.conta.toComposeColorConta
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyBackground
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyOutline
import com.openmoney.app.ui.theme.OpenMoneyTextPrimary
import com.openmoney.app.ui.theme.OpenMoneyTextSecondary
import java.math.BigDecimal
import java.math.RoundingMode

private val CorFiltroSelecionado = Color(0xFFD8F2AA)
private val CorTextoFiltroSelecionado = Color(0xFF557D18)
private val CorCardReceita = Color(0xFFD8F2AA)
private val CorTextoCardReceita = Color(0xFF5A861B)
private val CorCardDespesa = Color(0xFFE79294)
private val CorTextoCardDespesa = Color(0xFFA43434)
private val CorCardSaldo = Color(0xFF7BB0E4)
private val CorTextoCardSaldo = Color(0xFF2A5F97)
private val CorBarraReceita = Color(0xFF29B084)
private val CorBarraDespesa = Color(0xFFF05555)
private val CorOutrosGrafico = Color(0xFFC83F3F)
private val CorFundoRosca = Color(0xFF403A3A)

private enum class FiltroTipoRelatorio(
    val rotulo: String,
) {
    TODOS("Todos"),
    RECEITAS("Receitas"),
    DESPESAS("Despesas"),
}

private data class ItemGraficoCategoriaRelatorio(
    val categoriaNome: String,
    val corCategoria: Color,
    val total: BigDecimal,
    val percentual: Int,
)

@Composable
fun TelaRelatorios(
    estado: EstadoTelaRelatorios,
    aoClicarMenu: () -> Unit,
    aoSelecionarPeriodo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var filtroSelecionado by rememberSaveable {
        mutableStateOf(FiltroTipoRelatorio.TODOS)
    }

    val itensCategoriaBase = when (filtroSelecionado) {
        FiltroTipoRelatorio.RECEITAS -> estado.receitasPorCategoria
        FiltroTipoRelatorio.TODOS, FiltroTipoRelatorio.DESPESAS -> estado.despesasPorCategoria
    }
    val tituloCategoria = when (filtroSelecionado) {
        FiltroTipoRelatorio.RECEITAS -> "Receitas por categoria"
        FiltroTipoRelatorio.TODOS, FiltroTipoRelatorio.DESPESAS -> "Despesas por categoria"
    }
    val itensGraficoCategoria = prepararItensGraficoCategoria(itensCategoriaBase)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = OpenMoneyBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Relatorios",
                subtitulo = "Analise financeira",
                aoClicarAcaoEsquerda = aoClicarMenu,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.MENU,
            )

            if (estado.carregando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = OpenMoneyGreen)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                ) {
                    FiltrosRelatorio(
                        identificadorPeriodoSelecionado = estado.identificadorPeriodoSelecionado,
                        periodoReferencia = estado.periodoReferencia,
                        periodosDisponiveis = estado.periodosDisponiveis,
                        filtroSelecionado = filtroSelecionado,
                        aoSelecionarPeriodo = aoSelecionarPeriodo,
                        aoSelecionarFiltro = { filtroSelecionado = it },
                    )

                    Spacer(modifier = Modifier.height(34.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CartaoResumoAnalitico(
                            titulo = "Receitas",
                            valor = formatarMoedaOpenMoney(estado.totalReceitas),
                            corFundo = CorCardReceita,
                            corValor = CorTextoCardReceita,
                            corTitulo = CorTextoCardReceita,
                            modifier = Modifier.weight(1f),
                        )

                        CartaoResumoAnalitico(
                            titulo = "Despesas",
                            valor = formatarMoedaOpenMoney(estado.totalDespesas),
                            corFundo = CorCardDespesa,
                            corValor = CorTextoCardDespesa,
                            corTitulo = CorTextoCardDespesa,
                            modifier = Modifier.weight(1f),
                        )

                        CartaoResumoAnalitico(
                            titulo = "Saldo",
                            valor = formatarMoedaOpenMoney(estado.saldoMovimentado),
                            corFundo = CorCardSaldo,
                            corValor = CorTextoCardSaldo,
                            corTitulo = CorTextoCardSaldo,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Spacer(modifier = Modifier.height(38.dp))

                    Text(
                        text = tituloCategoria,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = OpenMoneyTextPrimary,
                        ),
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    if (itensGraficoCategoria.isEmpty()) {
                        MensagemSemDadosRelatorio(
                            texto = "Nenhuma movimentacao encontrada para este filtro.",
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            GraficoRoscaCategoriasRelatorio(
                                itens = itensGraficoCategoria,
                                modifier = Modifier.weight(1f),
                            )

                            Spacer(modifier = Modifier.width(18.dp))

                            LegendaCategoriasRelatorio(
                                itens = itensGraficoCategoria,
                                modifier = Modifier.weight(1.15f),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(54.dp))

                    Text(
                        text = "Receitas x Despesas (ultimos 4 meses)",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = OpenMoneyTextPrimary,
                        ),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    GraficoComparativoMensalRelatorio(
                        itens = estado.comparativoMensal,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun FiltrosRelatorio(
    identificadorPeriodoSelecionado: String,
    periodoReferencia: String,
    periodosDisponiveis: List<ItemPeriodoRelatorio>,
    filtroSelecionado: FiltroTipoRelatorio,
    aoSelecionarPeriodo: (String) -> Unit,
    aoSelecionarFiltro: (FiltroTipoRelatorio) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SeletorPeriodoRelatorio(
                identificadorSelecionado = identificadorPeriodoSelecionado,
                texto = periodoReferencia.ifBlank { "Periodo atual" },
                periodosDisponiveis = periodosDisponiveis,
                aoSelecionarPeriodo = aoSelecionarPeriodo,
                modifier = Modifier.weight(1.25f),
            )

            ChipFiltroRelatorio(
                texto = FiltroTipoRelatorio.TODOS.rotulo,
                selecionado = filtroSelecionado == FiltroTipoRelatorio.TODOS,
                aoClicar = { aoSelecionarFiltro(FiltroTipoRelatorio.TODOS) },
                modifier = Modifier.weight(0.78f),
            )

            ChipFiltroRelatorio(
                texto = FiltroTipoRelatorio.RECEITAS.rotulo,
                selecionado = filtroSelecionado == FiltroTipoRelatorio.RECEITAS,
                aoClicar = { aoSelecionarFiltro(FiltroTipoRelatorio.RECEITAS) },
                modifier = Modifier.weight(0.92f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            ChipFiltroRelatorio(
                texto = FiltroTipoRelatorio.DESPESAS.rotulo,
                selecionado = filtroSelecionado == FiltroTipoRelatorio.DESPESAS,
                aoClicar = { aoSelecionarFiltro(FiltroTipoRelatorio.DESPESAS) },
                modifier = Modifier.width(152.dp),
            )
        }
    }
}

@Composable
private fun SeletorPeriodoRelatorio(
    identificadorSelecionado: String,
    texto: String,
    periodosDisponiveis: List<ItemPeriodoRelatorio>,
    aoSelecionarPeriodo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpandido by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        Surface(
            onClick = {
                if (periodosDisponiveis.isNotEmpty()) {
                    menuExpandido = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, OpenMoneyOutline.copy(alpha = 0.95f)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = texto,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = OpenMoneyTextSecondary,
                    ),
                )

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Selecionar periodo",
                    tint = OpenMoneyTextSecondary,
                )
            }
        }

        DropdownMenu(
            expanded = menuExpandido,
            onDismissRequest = { menuExpandido = false },
            modifier = Modifier.width(maxWidth),
        ) {
            periodosDisponiveis.forEach { periodo ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = periodo.rotulo,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = if (periodo.identificador == identificadorSelecionado) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                },
                                color = if (periodo.identificador == identificadorSelecionado) {
                                    OpenMoneyGreen
                                } else {
                                    OpenMoneyTextPrimary
                                },
                            ),
                        )
                    },
                    onClick = {
                        menuExpandido = false
                        aoSelecionarPeriodo(periodo.identificador)
                    },
                )
            }
        }
    }
}

@Composable
private fun ChipFiltroRelatorio(
    texto: String,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = aoClicar,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = if (selecionado) CorFiltroSelecionado else Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = if (selecionado) {
                Color.Transparent
            } else {
                OpenMoneyOutline.copy(alpha = 0.95f)
            },
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = texto,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (selecionado) {
                        CorTextoFiltroSelecionado
                    } else {
                        OpenMoneyTextSecondary
                    },
                ),
            )
        }
    }
}

@Composable
private fun CartaoResumoAnalitico(
    titulo: String,
    valor: String,
    corFundo: Color,
    corTitulo: Color,
    corValor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = corFundo,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = titulo,
                style = TextStyle(
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = corTitulo,
                ),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = valor,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = corValor,
                ),
            )
        }
    }
}

@Composable
private fun GraficoRoscaCategoriasRelatorio(
    itens: List<ItemGraficoCategoriaRelatorio>,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.size(170.dp),
    ) {
        val strokeWidth = size.minDimension * 0.13f
        val diametro = size.minDimension - strokeWidth
        var anguloInicial = -90f

        drawArc(
            color = CorFundoRosca,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(
                x = strokeWidth / 2f,
                y = strokeWidth / 2f,
            ),
            size = Size(diametro, diametro),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
        )

        itens.forEach { item ->
            val angulo = (item.percentual / 100f) * 360f
            drawArc(
                color = item.corCategoria,
                startAngle = anguloInicial,
                sweepAngle = angulo,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(
                    x = strokeWidth / 2f,
                    y = strokeWidth / 2f,
                ),
                size = Size(diametro, diametro),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            )
            anguloInicial += angulo
        }
    }
}

@Composable
private fun LegendaCategoriasRelatorio(
    itens: List<ItemGraficoCategoriaRelatorio>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itens.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(item.corCategoria, CircleShape),
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = item.categoriaNome,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = OpenMoneyTextSecondary,
                    ),
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${item.percentual}%",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = OpenMoneyTextSecondary,
                    ),
                )
            }
        }
    }
}

@Composable
private fun GraficoComparativoMensalRelatorio(
    itens: List<ItemComparativoMensalRelatorio>,
) {
    if (itens.isEmpty()) {
        MensagemSemDadosRelatorio(
            texto = "Ainda nao existem meses suficientes para o comparativo.",
        )
        return
    }

    val maiorValor = itens
        .flatMap { listOf(it.totalReceitas, it.totalDespesas) }
        .maxOrNull()
        ?.takeIf { it > BigDecimal.ZERO }
        ?: BigDecimal.ONE

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
        ) {
            itens.forEach { item ->
                ColunaComparativoMensalRelatorio(
                    item = item,
                    maiorValor = maiorValor,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(OpenMoneyOutline.copy(alpha = 0.78f)),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            LegendaComparativoMensalRelatorio(
                cor = CorBarraReceita,
                texto = "Receitas",
            )

            Spacer(modifier = Modifier.width(34.dp))

            LegendaComparativoMensalRelatorio(
                cor = CorBarraDespesa,
                texto = "Despesas",
            )
        }
    }
}

@Composable
private fun ColunaComparativoMensalRelatorio(
    item: ItemComparativoMensalRelatorio,
    maiorValor: BigDecimal,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            BarraVerticalComparativoRelatorio(
                valor = item.totalReceitas,
                maiorValor = maiorValor,
                cor = CorBarraReceita,
            )

            BarraVerticalComparativoRelatorio(
                valor = item.totalDespesas,
                maiorValor = maiorValor,
                cor = CorBarraDespesa,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.rotuloMes,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = if (item.emDestaque) FontWeight.Medium else FontWeight.Normal,
                color = if (item.emDestaque) OpenMoneyGreen else OpenMoneyTextSecondary,
            ),
        )
    }
}

@Composable
private fun BarraVerticalComparativoRelatorio(
    valor: BigDecimal,
    maiorValor: BigDecimal,
    cor: Color,
) {
    val proporcao = if (maiorValor.compareTo(BigDecimal.ZERO) == 0) {
        0f
    } else {
        valor.divide(maiorValor, 4, RoundingMode.HALF_UP)
            .toFloat()
            .coerceIn(0f, 1f)
    }
    val alturaMinima = 6.dp
    val alturaMaxima = 92.dp
    val alturaBarra = alturaMinima + (alturaMaxima - alturaMinima) * proporcao

    Box(
        modifier = Modifier
            .width(20.dp)
            .height(alturaBarra)
            .background(
                color = cor,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            ),
    )
}

@Composable
private fun LegendaComparativoMensalRelatorio(
    cor: Color,
    texto: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(cor),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = texto,
            style = TextStyle(
                fontSize = 15.sp,
                color = OpenMoneyTextSecondary,
            ),
        )
    }
}

@Composable
private fun MensagemSemDadosRelatorio(
    texto: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF231F1F),
        border = BorderStroke(1.dp, OpenMoneyOutline.copy(alpha = 0.18f)),
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            style = TextStyle(
                fontSize = 15.sp,
                color = OpenMoneyTextSecondary,
            ),
        )
    }
}

private fun prepararItensGraficoCategoria(
    itens: List<ItemResumoCategoriaRelatorio>,
): List<ItemGraficoCategoriaRelatorio> {
    if (itens.isEmpty()) {
        return emptyList()
    }

    val itensOrdenados = itens.sortedByDescending(ItemResumoCategoriaRelatorio::total)
    val itensNormalizados = if (itensOrdenados.size <= 4) {
        itensOrdenados
    } else {
        val principais = itensOrdenados.take(3)
        val outrosTotal = itensOrdenados.drop(3)
            .fold(BigDecimal.ZERO) { acumulado, item -> acumulado + item.total }

        principais + ItemResumoCategoriaRelatorio(
            categoriaNome = "Outros",
            corCategoria = "#C83F3F",
            total = outrosTotal,
        )
    }

    val totalGeral = itensNormalizados
        .fold(BigDecimal.ZERO) { acumulado, item -> acumulado + item.total }
        .takeIf { it > BigDecimal.ZERO }
        ?: return emptyList()

    var percentualAcumulado = 0

    return itensNormalizados.mapIndexed { indice, item ->
        val percentual = if (indice == itensNormalizados.lastIndex) {
            100 - percentualAcumulado
        } else {
            item.total
                .multiply(BigDecimal(100))
                .divide(totalGeral, 0, RoundingMode.HALF_UP)
                .toInt()
        }

        percentualAcumulado += percentual

        ItemGraficoCategoriaRelatorio(
            categoriaNome = item.categoriaNome,
            corCategoria = if (item.categoriaNome == "Outros") {
                CorOutrosGrafico
            } else {
                item.corCategoria.toComposeColorConta()
            },
            total = item.total,
            percentual = percentual.coerceAtLeast(0),
        )
    }
}
