package com.openmoney.app.ui.meta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.domain.comum.ConversorEntradaFinanceira
import com.openmoney.app.ui.autenticacao.CampoAutenticacao
import com.openmoney.app.ui.autenticacao.RotuloCampoAutenticacao
import com.openmoney.app.ui.principal.CabecalhoSecaoAutenticada
import com.openmoney.app.ui.principal.TipoAcaoCabecalhoEsquerda
import com.openmoney.app.ui.theme.OpenMoneyGreen
import java.math.BigDecimal

@Composable
fun TelaCriarMeta(
    nomeMeta: String,
    valorMeta: String,
    valorAtual: String,
    dataLimite: String,
    erroNomeMeta: String?,
    erroValorMeta: String?,
    erroValorAtual: String?,
    erroDataLimite: String?,
    mensagemErro: String?,
    aoAlterarNomeMeta: (String) -> Unit,
    aoAlterarValorMeta: (String) -> Unit,
    aoAlterarValorAtual: (String) -> Unit,
    aoAlterarDataLimite: (String) -> Unit,
    aoClicarVoltar: () -> Unit,
    aoClicarCancelar: () -> Unit,
    aoClicarSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val resumoPreview = calcularResumoMeta(
        valorMeta = ConversorEntradaFinanceira.converterValorMonetario(valorMeta),
        valorAtual = if (valorAtual.isBlank()) {
            BigDecimal.ZERO
        } else {
            ConversorEntradaFinanceira.converterValorMonetario(valorAtual)
        },
        dataLimite = ConversorEntradaFinanceira.converterDataBrasil(dataLimite),
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
        ) {
            CabecalhoSecaoAutenticada(
                titulo = "Criar meta",
                aoClicarAcaoEsquerda = aoClicarVoltar,
                tipoAcaoEsquerda = TipoAcaoCabecalhoEsquerda.VOLTAR,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 26.dp),
            ) {
                RotuloCampoAutenticacao(texto = "Descricao")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = nomeMeta,
                    aoAlterarValor = aoAlterarNomeMeta,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroNomeMeta,
                    placeholder = "Nome da meta (ex: Memoria RAM)",
                )

                Spacer(modifier = Modifier.height(28.dp))

                RotuloCampoAutenticacao(texto = "Total meta")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = valorMeta,
                    aoAlterarValor = aoAlterarValorMeta,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroValorMeta,
                    placeholder = "R$ 0,00",
                )

                Spacer(modifier = Modifier.height(28.dp))

                RotuloCampoAutenticacao(texto = "Valor atual")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = valorAtual,
                    aoAlterarValor = aoAlterarValorAtual,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    mensagemErro = erroValorAtual,
                    placeholder = "R$ 0,00 (pode ser zero)",
                )

                Spacer(modifier = Modifier.height(28.dp))

                RotuloCampoAutenticacao(texto = "Data limite")
                Spacer(modifier = Modifier.height(10.dp))
                CampoAutenticacao(
                    valor = dataLimite,
                    aoAlterarValor = aoAlterarDataLimite,
                    opcoesTeclado = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    acoesTeclado = KeyboardActions(onDone = { aoClicarSalvar() }),
                    mensagemErro = erroDataLimite,
                    placeholder = "DD/MM/AAAA",
                )

                Spacer(modifier = Modifier.height(34.dp))

                CartaoResumoMeta(
                    titulo = "Preview da meta",
                    resumo = resumoPreview,
                )

                if (mensagemErro != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = mensagemErro,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 14.sp),
                    )
                }

                Spacer(modifier = Modifier.height(38.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    OutlinedButton(
                        onClick = aoClicarCancelar,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.error,
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Text(
                            text = "Cancelar",
                            style = TextStyle(fontSize = 18.sp),
                        )
                    }

                    Button(
                        onClick = aoClicarSalvar,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OpenMoneyGreen,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Text(
                            text = "Criar Meta",
                            style = TextStyle(fontSize = 18.sp),
                        )
                    }
                }
            }
        }
    }
}
