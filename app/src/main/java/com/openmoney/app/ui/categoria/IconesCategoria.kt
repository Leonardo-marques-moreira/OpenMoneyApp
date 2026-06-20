package com.openmoney.app.ui.categoria

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

data class IconeCategoriaDisponivel(
    val codigo: String,
    val descricao: String,
    val imagem: ImageVector,
)

val IconesCategoriaDisponiveis = listOf(
    IconeCategoriaDisponivel("wallet", "Carteira", Icons.Outlined.AccountBalanceWallet),
    IconeCategoriaDisponivel("shopping_cart", "Mercado", Icons.Outlined.ShoppingCart),
    IconeCategoriaDisponivel("home", "Casa", Icons.Outlined.Home),
    IconeCategoriaDisponivel("pets", "Pet", Icons.Outlined.Pets),
    IconeCategoriaDisponivel("restaurant", "Restaurante", Icons.Outlined.Restaurant),
    IconeCategoriaDisponivel("school", "Educacao", Icons.Outlined.School),
    IconeCategoriaDisponivel("healing", "Saude", Icons.Outlined.Healing),
    IconeCategoriaDisponivel("directions_bus", "Transporte", Icons.Outlined.DirectionsBus),
    IconeCategoriaDisponivel("flight", "Viagem", Icons.Outlined.Flight),
    IconeCategoriaDisponivel("sports_esports", "Lazer", Icons.Outlined.SportsEsports),
)

fun resolverIconeCategoriaDisponivel(codigo: String): IconeCategoriaDisponivel {
    return IconesCategoriaDisponiveis.firstOrNull { it.codigo == codigo }
        ?: IconesCategoriaDisponiveis.first()
}
