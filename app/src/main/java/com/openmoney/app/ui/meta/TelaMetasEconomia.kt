package com.openmoney.app.ui.meta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.model.MetaEconomia
import com.openmoney.app.ui.principal.BotaoAcaoCabecalho
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen

@Composable
fun TelaMetasEconomia(
    metas: List<MetaEconomia>,
    carregando: Boolean,
    mensagemSucesso: String?,
    aoClicarVoltar: () -> Unit,
    aoClicarNovaMeta: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                titulo = "Metas de economia",
                aoClicarAcaoEsquerda = aoClicarVoltar,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
                conteudoDireita = {
                    BotaoAcaoCabecalho(
                        texto = "+ Nova",
                        aoClicar = aoClicarNovaMeta,
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
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

                    metas.isEmpty() -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                            color = androidx.compose.ui.graphics.Color(0xFF1F1C1C),
                        ) {
                            Text(
                                text = "Nenhuma meta cadastrada ate o momento. Toque em + Nova para criar a primeira meta.",
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
                            metas.forEach { meta ->
                                CartaoResumoMeta(
                                    titulo = meta.nome,
                                    resumo = calcularResumoMeta(
                                        valorMeta = meta.valorMeta,
                                        valorAtual = meta.valorAtual,
                                        dataLimite = meta.dataLimite,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
