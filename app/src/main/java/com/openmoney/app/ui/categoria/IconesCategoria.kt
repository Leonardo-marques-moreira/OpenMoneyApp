package com.openmoney.app.ui.categoria

data class IconeCategoriaDisponivel(
    val codigo: String,
    val descricao: String,
)

val IconesCategoriaDisponiveis = listOf(
    IconeCategoriaDisponivel("wallet", "Carteira"),
    IconeCategoriaDisponivel("shopping_cart", "Mercado"),
    IconeCategoriaDisponivel("home", "Casa"),
    IconeCategoriaDisponivel("pets", "Pet"),
    IconeCategoriaDisponivel("restaurant", "Restaurante"),
    IconeCategoriaDisponivel("school", "Educacao"),
    IconeCategoriaDisponivel("healing", "Saude"),
    IconeCategoriaDisponivel("directions_bus", "Transporte"),
    IconeCategoriaDisponivel("flight", "Viagem"),
    IconeCategoriaDisponivel("sports_esports", "Lazer"),
)