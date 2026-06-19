package com.openmoney.app.ui.autenticacao

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.ui.theme.OpenMoneyGreen
import com.openmoney.app.ui.theme.OpenMoneyTextPrimary
import com.openmoney.app.ui.theme.OpenMoneyTextSecondary

@Composable
fun RotuloCampoAutenticacao(texto: String) {
    Text(
        text = texto,
        modifier = Modifier.fillMaxWidth(),
        color = OpenMoneyTextSecondary,
        style = TextStyle(fontSize = 16.sp),
    )
}

@Composable
fun CampoAutenticacao(
    valor: String,
    aoAlterarValor: (String) -> Unit,
    opcoesTeclado: KeyboardOptions,
    modifier: Modifier = Modifier,
    acoesTeclado: KeyboardActions = KeyboardActions.Default,
    transformacaoVisual: VisualTransformation = VisualTransformation.None,
    iconeFinal: @Composable (() -> Unit)? = null,
    mensagemErro: String? = null,
    placeholder: String? = null,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterarValor,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = OpenMoneyTextPrimary,
        ),
        visualTransformation = transformacaoVisual,
        keyboardOptions = opcoesTeclado,
        keyboardActions = acoesTeclado,
        shape = RoundedCornerShape(18.dp),
        trailingIcon = iconeFinal,
        isError = mensagemErro != null,
        placeholder = {
            if (!placeholder.isNullOrBlank()) {
                Text(
                    text = placeholder,
                    color = OpenMoneyTextSecondary.copy(alpha = 0.85f),
                    style = TextStyle(fontSize = 18.sp),
                )
            }
        },
        supportingText = {
            if (mensagemErro != null) {
                Text(text = mensagemErro)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = OpenMoneyTextPrimary,
            unfocusedTextColor = OpenMoneyTextPrimary,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            cursorColor = OpenMoneyGreen,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
            errorTrailingIconColor = MaterialTheme.colorScheme.onBackground,
            focusedPlaceholderColor = OpenMoneyTextSecondary.copy(alpha = 0.85f),
            unfocusedPlaceholderColor = OpenMoneyTextSecondary.copy(alpha = 0.85f),
        ),
    )
}
