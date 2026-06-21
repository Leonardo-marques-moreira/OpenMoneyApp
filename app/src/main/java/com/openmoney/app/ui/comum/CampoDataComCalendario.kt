package com.openmoney.app.ui.comum

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.openmoney.app.domain.comum.ConversorEntradaFinanceira
import com.openmoney.app.ui.autenticacao.CampoAutenticacao
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val formatadorDataBrasilCampo = DateTimeFormatter.ofPattern("dd/MM/uuuu")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDataComCalendario(
    valor: String,
    aoAlterarValor: (String) -> Unit,
    mensagemErro: String?,
    modifier: Modifier = Modifier,
    placeholder: String = "DD/MM/AAAA",
    imeAction: ImeAction = ImeAction.Next,
    acoesTeclado: KeyboardActions = KeyboardActions.Default,
    bloquearDatasAnteriores: Boolean = false,
) {
    var exibirCalendario by rememberSaveable { mutableStateOf(false) }
    val hoje = remember { LocalDate.now() }
    val dataInicial = remember(valor) {
        ConversorEntradaFinanceira.converterDataBrasil(valor) ?: hoje
    }

    CampoAutenticacao(
        valor = valor,
        aoAlterarValor = aoAlterarValor,
        modifier = modifier,
        opcoesTeclado = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        acoesTeclado = acoesTeclado,
        mensagemErro = mensagemErro,
        placeholder = placeholder,
        iconeFinal = {
            IconButton(onClick = { exibirCalendario = true }) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Abrir calendario",
                )
            }
        },
    )

    if (exibirCalendario) {
        val estadoCalendario = rememberDatePickerState(
            initialSelectedDateMillis = dataInicial.paraUtcMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    if (!bloquearDatasAnteriores) {
                        return true
                    }

                    return !utcTimeMillis.paraLocalDateUtc().isBefore(hoje)
                }
            },
        )

        DatePickerDialog(
            onDismissRequest = { exibirCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        estadoCalendario.selectedDateMillis?.let { dataSelecionada ->
                            aoAlterarValor(
                                dataSelecionada
                                    .paraLocalDateUtc()
                                    .format(formatadorDataBrasilCampo),
                            )
                        }
                        exibirCalendario = false
                    },
                ) {
                    Text("Selecionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { exibirCalendario = false }) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(
                state = estadoCalendario,
                title = null,
                headline = null,
                showModeToggle = false,
            )
        }
    }
}

private fun LocalDate.paraUtcMillis(): Long {
    return atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
}

private fun Long.paraLocalDateUtc(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}
