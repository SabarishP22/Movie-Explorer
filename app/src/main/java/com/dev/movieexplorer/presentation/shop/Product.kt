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

enum class TrackingStage(val label: String) {
    Placed("Order placed"),
    Packed("Packed"),
    Shipped("Shipped"),
    OutForDelivery("Out for delivery"),
    Delivered("Delivered"),
}

data class TrackingEvent(
    val title: String,
    val detail: String,
    val time: String,
)

data class ShopOrder(
    val id: String,
    val lines: List<CartLine>,
    val total: Double,
    val stage: TrackingStage,
    val eta: String,
    val placedOn: String,
    val address: String,
    val carrier: String,
    val events: List<TrackingEvent>,
)

data class SavedAddress(
    val id: String,
    val label: String,
    val person: String,
    val lines: String,
    val phone: String,
)

data class SavedCard(
    val id: String,
    val brand: String,
    val last4: String,
    val expiry: String,
    val color: Color,
)

data class Coupon(
    val code: String,
    val title: String,
    val detail: String,
    val off: Double,
)

data class ShopNotification(
    val id: String,
    val title: String,
    val body: String,
    val time: String,
    val read: Boolean,
)

data class ShopProfile(
    val name: String,
    val email: String,
    val phone: String,
    val membership: String,
    val memberSince: String,
)
