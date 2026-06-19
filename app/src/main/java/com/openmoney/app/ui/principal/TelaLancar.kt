package com.openmoney.app.ui.principal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.Categoria
import com.openmoney.app.ui.theme.OpenMoneyGreen

@Composable
fun TelaLancar(
    categorias: List<Categoria>,
    mensagemSucesso: String?,
    aoVoltarInicio: () -> Unit,
    aoAbrirNovaCategoria: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Lancar",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                    )

                    OutlinedButton(
                        onClick = aoVoltarInicio,
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(text = "Inicio")
                    }
                }
            }

            item {
                Text(
                    text = "Organize primeiro as categorias. As telas de transacao e despesa entram na sequencia.",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                )
            }

            if (mensagemSucesso != null) {
                item {
                    Text(
                        text = mensagemSucesso,
                        color = OpenMoneyGreen,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text(
                            text = "Nova transacao",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                        )
                        Text(
                            text = "Fluxo previsto na proxima etapa.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary,
                            ),
                        )
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = "Em breve")
                        }
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text(
                            text = "Nova categoria",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                        )
                        Text(
                            text = "Crie as categorias que depois serao usadas nas transacoes.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary,
                            ),
                        )
                        Button(
                            onClick = aoAbrirNovaCategoria,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OpenMoneyGreen,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                        ) {
                            Text(text = "Abrir categoria")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Categorias disponiveis",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                )
            }

            if (categorias.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Text(
                            text = "Nenhuma categoria cadastrada ate o momento.",
                            modifier = Modifier.padding(18.dp),
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary,
                            ),
                        )
                    }
                }
            } else {
                items(categorias) { categoria ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = categoria.nome,
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                ),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = categoria.tipo.descricao,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}