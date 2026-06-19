package com.openmoney.app.ui.meta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTextPrimary
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

private val CorCartaoMeta = Color(0xFF1F1C1C)
private val CorTrilhaProgressoMeta = Color(0xFF2A2A2A)
private val CorAmareloMeta = Color(0xFF9E791D)
private val CorFundoAmareloMeta = Color(0xFFE0CA92)
private val CorVerdeMetaEscuro = Color(0xFF3B6D11)
private val CorFundoVerdeMeta = Color(0xFFD4EBC1)
private val CorVermelhoMeta = Color(0xFFA32D2D)
private val CorFundoVermelhoMeta = Color(0xFFD68686)

internal data class ResumoProgressoMeta(
    val percentual: Int,
    val fracao: Float,
    val textoValores: String,
    val status: StatusMetaUi,
)

internal enum class StatusMetaUi(
    val descricao: String,
    val corBarra: Color,
    val corFundo: Color,
    val corBorda: Color,
    val corTexto: Color,
) {
    AGUARDANDO(
        descricao = "Aguardando",
        corBarra = MaterialThemeColorTokens.Neutro,
        corFundo = Color.Transparent,
        corBorda = MaterialThemeColorTokens.Outline,
        corTexto = OpenMoneyTextPrimary,
    ),
    EM_ANDAMENTO(
        descricao = "Em andamento",
        corBarra = CorAmareloMeta,
        corFundo = CorFundoAmareloMeta,
        corBorda = CorAmareloMeta,
        corTexto = CorAmareloMeta,
    ),
    NO_PRAZO(
        descricao = "No prazo",
        corBarra = OpenMoneyGreen,
        corFundo = CorFundoVerdeMeta,
        corBorda = CorVerdeMetaEscuro,
        corTexto = CorVerdeMetaEscuro,
    ),
    ATRASADO(
        descricao = "Atrasado",
        corBarra = CorVermelhoMeta,
        corFundo = CorFundoVermelhoMeta,
        corBorda = CorVermelhoMeta,
        corTexto = CorVermelhoMeta,
    ),
    CONCLUIDA(
        descricao = "Concluida",
        corBarra = OpenMoneyGreen,
        corFundo = CorFundoVerdeMeta,
        corBorda = CorVerdeMetaEscuro,
        corTexto = CorVerdeMetaEscuro,
    );
}

private object MaterialThemeColorTokens {
    val Outline = Color(0xFFD9D9D9)
    val Neutro = Color(0xFF888888)
}

internal fun calcularResumoMeta(
    valorMeta: BigDecimal?,
    valorAtual: BigDecimal?,
    dataLimite: LocalDate?,
    dataReferencia: LocalDate = LocalDate.now(),
): ResumoProgressoMeta {
    val metaSegura = valorMeta
        ?.takeIf { it.compareTo(BigDecimal.ZERO) > 0 }
        ?: BigDecimal.ZERO

    val atualSeguro = when {
        valorAtual == null -> BigDecimal.ZERO
        valorAtual.compareTo(BigDecimal.ZERO) < 0 -> BigDecimal.ZERO
        else -> valorAtual
    }

    val fracaoBruta = if (metaSegura.compareTo(BigDecimal.ZERO) > 0) {
        atualSeguro.divide(metaSegura, 4, RoundingMode.HALF_UP)
    } else {
        BigDecimal.ZERO
    }

    val percentual = fracaoBruta
        .multiply(BigDecimal("100"))
        .setScale(0, RoundingMode.HALF_UP)
        .toInt()
        .coerceIn(0, 100)

    val fracao = when {
        fracaoBruta.compareTo(BigDecimal.ZERO) <= 0 -> 0f
        fracaoBruta.compareTo(BigDecimal.ONE) >= 0 -> 1f
        else -> fracaoBruta.toFloat()
    }

    val status = when {
        metaSegura.compareTo(BigDecimal.ZERO) <= 0 -> StatusMetaUi.AGUARDANDO
        atualSeguro.compareTo(BigDecimal.ZERO) == 0 -> StatusMetaUi.AGUARDANDO
        atualSeguro.compareTo(metaSegura) >= 0 -> StatusMetaUi.CONCLUIDA
        dataLimite != null && dataLimite.isBefore(dataReferencia) -> StatusMetaUi.ATRASADO
        percentual >= 50 -> StatusMetaUi.NO_PRAZO
        else -> StatusMetaUi.EM_ANDAMENTO
    }

    return ResumoProgressoMeta(
        percentual = percentual,
        fracao = fracao,
        textoValores = "${formatarMoedaOpenMoney(atualSeguro)} de ${formatarMoedaOpenMoney(metaSegura)}",
        status = status,
    )
}

@Composable
internal fun CartaoResumoMeta(
    titulo: String,
    resumo: ResumoProgressoMeta,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CorCartaoMeta,
        border = BorderStroke(1.dp, MaterialThemeColorTokens.Outline),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = titulo,
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = OpenMoneyTextPrimary,
                    ),
                )
                Text(
                    text = "${resumo.percentual}%",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = OpenMoneyTextPrimary,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            BarraProgressoMeta(
                fracao = resumo.fracao,
                corBarra = resumo.status.corBarra,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = resumo.textoValores,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = OpenMoneyTextPrimary,
                    ),
                )
                ChipStatusMeta(status = resumo.status)
            }
        }
    }
}

@Composable
private fun BarraProgressoMeta(
    fracao: Float,
    corBarra: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(11.dp)
            .background(
                color = CorTrilhaProgressoMeta,
                shape = RoundedCornerShape(20.dp),
            ),
    ) {
        if (fracao > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fracao.coerceIn(0f, 1f))
                    .height(11.dp)
                    .background(
                        color = corBarra,
                        shape = RoundedCornerShape(20.dp),
                    ),
            )
        }
    }
}

@Composable
private fun ChipStatusMeta(
    status: StatusMetaUi,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (status.corFundo == Color.Transparent) CorCartaoMeta else status.corFundo,
        border = BorderStroke(1.dp, status.corBorda),
    ) {
        Text(
            text = status.descricao,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = status.corTexto,
            ),
        )
    }
}
