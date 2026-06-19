package com.openmoney.app.ui.conta

import androidx.compose.ui.graphics.Color
import com.openmoney.app.ui.theme.OpenMoneyTextSecondary
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

private val CorValorPositivo = Color(0xFF4B8A11)
private val CorValorNegativo = Color(0xFFA32D2D)

fun formatarMoedaOpenMoney(valor: BigDecimal): String {
    val localidadeBrasil = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()
    val formatador = NumberFormat.getCurrencyInstance(localidadeBrasil)
    return formatador.format(valor)
}

fun formatarCorSaldoOpenMoney(valor: BigDecimal): Color {
    return if (valor.signum() >= 0) CorValorPositivo else CorValorNegativo
}

fun String.toComposeColorConta(): Color {
    return runCatching {
        Color(android.graphics.Color.parseColor(this))
    }.getOrElse {
        OpenMoneyTextSecondary
    }
}
