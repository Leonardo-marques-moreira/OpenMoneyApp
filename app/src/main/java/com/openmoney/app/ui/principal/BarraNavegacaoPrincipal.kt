package com.openmoney.app.ui.principal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

private val FundoBarraNavegacaoOpenMoney = Color(0xFF2A2A2A)
private val TextoBarraNavegacaoOpenMoney = Color(0xFFD9D9D9)

private data class ItemBarraNavegacaoPrincipal(
    val destino: DestinoAreaLogada,
    val titulo: String,
    val icone: ImageVector,
)

@Composable
fun BarraNavegacaoPrincipal(
    destinoSelecionado: DestinoAreaLogada,
    aoSelecionarDestino: (DestinoAreaLogada) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itens = listOf(
        ItemBarraNavegacaoPrincipal(
            destino = DestinoAreaLogada.CONTAS,
            titulo = "Contas",
            icone = Icons.Outlined.AccountBalanceWallet,
        ),
        ItemBarraNavegacaoPrincipal(
            destino = DestinoAreaLogada.METAS,
            titulo = "Metas",
            icone = Icons.Outlined.TrackChanges,
        ),
        ItemBarraNavegacaoPrincipal(
            destino = DestinoAreaLogada.LANCAR,
            titulo = "Lancar",
            icone = Icons.Outlined.AddCircle,
        ),
        ItemBarraNavegacaoPrincipal(
            destino = DestinoAreaLogada.RELATORIOS,
            titulo = "Relatorios",
            icone = Icons.Outlined.BarChart,
        ),
        ItemBarraNavegacaoPrincipal(
            destino = DestinoAreaLogada.PERFIL,
            titulo = "Perfil",
            icone = Icons.Outlined.Person,
        ),
    )

    val cores = NavigationBarItemDefaults.colors(
        selectedIconColor = TextoBarraNavegacaoOpenMoney,
        selectedTextColor = TextoBarraNavegacaoOpenMoney,
        unselectedIconColor = TextoBarraNavegacaoOpenMoney.copy(alpha = 0.82f),
        unselectedTextColor = TextoBarraNavegacaoOpenMoney.copy(alpha = 0.82f),
        indicatorColor = Color.Transparent,
    )

    NavigationBar(
        modifier = modifier,
        containerColor = FundoBarraNavegacaoOpenMoney,
    ) {
        itens.forEach { item ->
            NavigationBarItem(
                selected = destinoSelecionado == item.destino,
                onClick = { aoSelecionarDestino(item.destino) },
                colors = cores,
                icon = {
                    Icon(
                        imageVector = item.icone,
                        contentDescription = item.titulo,
                    )
                },
                label = {
                    Text(text = item.titulo)
                },
            )
        }
    }
}
