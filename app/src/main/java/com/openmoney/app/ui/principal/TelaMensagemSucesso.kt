package com.openmoney.app.ui.principal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TelaMensagemSucesso(
    mensagem: String,
    textoBotao: String,
    aoClicarBotao: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF211D1D),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(132.dp)
                        .background(Color(0xFF27A67D), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(modifier = Modifier.size(72.dp)) {
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width * 0.18f, size.height * 0.55f),
                            end = Offset(size.width * 0.42f, size.height * 0.78f),
                            strokeWidth = 10f,
                            cap = StrokeCap.Round,
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width * 0.42f, size.height * 0.78f),
                            end = Offset(size.width * 0.82f, size.height * 0.25f),
                            strokeWidth = 10f,
                            cap = StrokeCap.Round,
                        )
                    }
                }

                Text(
                    text = mensagem,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFFE8E8E8),
                    textAlign = TextAlign.Center,
                )

                Button(
                    onClick = aoClicarBotao,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF27A67D),
                        contentColor = Color(0xFFEFF8F4),
                    ),
                ) {
                    Text(
                        text = textoBotao,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B2828)
@Composable
private fun TelaMensagemSucessoPreview() {
    TelaMensagemSucesso(
        mensagem = "Cadastro realizado com sucesso",
        textoBotao = "Fazer login",
        aoClicarBotao = {},
    )
}
