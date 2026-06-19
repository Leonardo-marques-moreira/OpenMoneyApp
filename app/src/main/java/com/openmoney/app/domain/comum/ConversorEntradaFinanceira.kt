package com.openmoney.app.domain.comum

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

object ConversorEntradaFinanceira {

    private val formatadorDataBrasil = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    fun normalizarValorMonetario(valorInformado: String): String {
        val valorSemMascara = valorInformado
            .replace("R$", "", ignoreCase = true)
            .replace(" ", "")

        return if (valorSemMascara.contains(",")) {
            valorSemMascara
                .replace(".", "")
                .replace(",", ".")
        } else {
            valorSemMascara
        }
    }

    fun converterValorMonetario(valorInformado: String): BigDecimal? {
        val valorNormalizado = normalizarValorMonetario(valorInformado)
        if (valorNormalizado.isBlank()) {
            return null
        }

        return runCatching { BigDecimal(valorNormalizado) }.getOrNull()
    }

    fun converterDataBrasil(dataInformada: String): LocalDate? {
        if (dataInformada.isBlank()) {
            return null
        }

        return runCatching {
            LocalDate.parse(dataInformada.trim(), formatadorDataBrasil)
        }.getOrNull()
    }
}
