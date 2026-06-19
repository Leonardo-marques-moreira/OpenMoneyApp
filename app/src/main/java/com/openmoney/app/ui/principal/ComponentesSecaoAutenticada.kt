package com.openmoney.app.ui.principal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.ui.theme.OpenMoneyGreen

enum class TipoAcaoCabecalhoEsquerda {
    MENU,
    VOLTAR,
}

@Composable
fun CabecalhoSecaoAutenticada(
    titulo: String,
    aoClicarAcaoEsquerda: () -> Unit,
    tipoAcaoEsquerda: TipoAcaoCabecalhoEsquerda = TipoAcaoCabecalhoEsquerda.MENU,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    conteudoDireita: @Composable RowScope.() -> Unit = {},
    conteudoInferior: @Composable ColumnScope.() -> Unit = {},
) {
    val iconeAcaoEsquerda = when (tipoAcaoEsquerda) {
        TipoAcaoCabecalhoEsquerda.MENU -> Icons.Outlined.Menu
        TipoAcaoCabecalhoEsquerda.VOLTAR -> Icons.AutoMirrored.Outlined.ArrowBack
    }
    val descricaoAcaoEsquerda = when (tipoAcaoEsquerda) {
        TipoAcaoCabecalhoEsquerda.MENU -> "Abrir painel"
        TipoAcaoCabecalhoEsquerda.VOLTAR -> "Voltar"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = OpenMoneyGreen,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = aoClicarAcaoEsquerda,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(40.dp),
                ) {
                    Icon(
                        imageVector = iconeAcaoEsquerda,
                        contentDescription = descricaoAcaoEsquerda,
                        tint = Color(0xFF2A2A2A),
                        modifier = Modifier.size(30.dp),
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.TopEnd),
                    verticalAlignment = Alignment.CenterVertically,
                    content = conteudoDireita,
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = titulo,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )

                    if (!subtitulo.isNullOrBlank()) {
                        Text(
                            text = subtitulo,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
                            ),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = conteudoInferior,
            )
        }
    }
}

@Composable
fun BotaoAcaoCabecalho(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = aoClicar,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = OpenMoneyGreen,
        ),
    ) {
        Text(
            text = texto,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}
