package com.openmoney.app.ui.principal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openmoney.app.ui.theme.OpenMoneyGreen

private val FundoMenuLateralOpenMoney = Color(0xFF2A2A2A)
private val TextoMenuLateralOpenMoney = Color(0xFFF1F1F1)

private data class ItemMenuLateral(
    val titulo: String,
    val icone: ImageVector,
    val aoClicar: () -> Unit,
)

@Composable
fun MenuLateralOpenMoney(
    aoClicarPerfil: () -> Unit,
    aoClicarInicio: () -> Unit,
    aoClicarLancar: () -> Unit,
    aoClicarRelatorios: () -> Unit,
    aoClicarContas: () -> Unit,
    aoClicarMetas: () -> Unit,
    aoClicarSair: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itens = listOf(
        ItemMenuLateral(
            titulo = "Perfil",
            icone = Icons.Outlined.Person,
            aoClicar = aoClicarPerfil,
        ),
        ItemMenuLateral(
            titulo = "Inicio",
            icone = Icons.Outlined.Home,
            aoClicar = aoClicarInicio,
        ),
        ItemMenuLateral(
            titulo = "Lancar",
            icone = Icons.Outlined.AddCircle,
            aoClicar = aoClicarLancar,
        ),
        ItemMenuLateral(
            titulo = "Relatorios",
            icone = Icons.Outlined.BarChart,
            aoClicar = aoClicarRelatorios,
        ),
        ItemMenuLateral(
            titulo = "Contas",
            icone = Icons.Outlined.AccountBalanceWallet,
            aoClicar = aoClicarContas,
        ),
        ItemMenuLateral(
            titulo = "Metas",
            icone = Icons.Outlined.TrackChanges,
            aoClicar = aoClicarMetas,
        ),
        ItemMenuLateral(
            titulo = "Sair",
            icone = Icons.AutoMirrored.Outlined.Logout,
            aoClicar = aoClicarSair,
        ),
    )

    ModalDrawerSheet(
        modifier = modifier
            .fillMaxHeight()
            .width(318.dp),
        drawerShape = RoundedCornerShape(ZeroCornerSize),
        drawerContainerColor = FundoMenuLateralOpenMoney,
        drawerContentColor = TextoMenuLateralOpenMoney,
        drawerTonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 74.dp),
            verticalArrangement = Arrangement.spacedBy(38.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            itens.forEach { item ->
                BotaoMenuLateral(
                    titulo = item.titulo,
                    icone = item.icone,
                    aoClicar = item.aoClicar,
                )
            }
        }
    }
}

@Composable
private fun BotaoMenuLateral(
    titulo: String,
    icone: ImageVector,
    aoClicar: () -> Unit,
) {
    Button(
        onClick = aoClicar,
        modifier = Modifier
            .width(248.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = OpenMoneyGreen,
            contentColor = TextoMenuLateralOpenMoney,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icone,
                contentDescription = titulo,
                modifier = Modifier.size(30.dp),
            )

            Text(
                text = titulo,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 22.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        }
    }
}
