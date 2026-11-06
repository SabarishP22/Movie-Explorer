package com.dev.movieexplorer.presentation.shop

import androidx.compose.ui.graphics.Color

data class Swatch(
    val name: String,
    val color: Color,
)

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val rating: Double,
    val reviewCount: Int,
    val category: String,
    val imageUrl: String,
    val fallback: Color,
    val colors: List<Swatch>,
    val badge: String?,
    val blurb: String,
)

data class CartLine(
    val productId: String,
    val colorName: String,
    val quantity: Int,
) {
    val key: String = "$productId:$colorName"
}

data class PlacedOrder(
    val lines: List<CartLine>,
    val total: Double,
)
