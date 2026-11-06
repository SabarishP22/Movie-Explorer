package com.dev.movieexplorer.presentation.shop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    var lines by mutableStateOf<List<CartLine>>(emptyList())
        private set

    var orders by mutableStateOf(SampleAccount.orders)
        private set

    var lastOrder by mutableStateOf<ShopOrder?>(null)
        private set

    var wishlist by mutableStateOf(SampleAccount.wishlist)
        private set

    var addresses by mutableStateOf(SampleAccount.addresses)
        private set

    var payments by mutableStateOf(SampleAccount.payments)
        private set

    var coupons by mutableStateOf(SampleAccount.coupons)
        private set

    var notifications by mutableStateOf(SampleAccount.notifications)
        private set

    var selectedAddressId by mutableStateOf(SampleAccount.addresses.first().id)
        private set

    var appliedCoupon by mutableStateOf<String?>(null)
        private set

    val profile: ShopProfile = SampleAccount.profile

    val itemCount: Int
        get() = lines.sumOf { it.quantity }

    val merchandiseTotal: Double
        get() = lines.sumOf { line ->
            val price = SampleCatalog.find(line.productId)?.price ?: 0.0
            price * line.quantity
        }

    val discount: Double
        get() = coupons.find { it.code == appliedCoupon }?.off ?: 0.0

    val total: Double
        get() = (merchandiseTotal - discount).coerceAtLeast(0.0)

    val selectedAddress: SavedAddress?
        get() = addresses.find { it.id == selectedAddressId }

    val unreadCount: Int
        get() = notifications.count { !it.read }

    fun add(productId: String, colorName: String) {
        val existing = lines.find { it.productId == productId && it.colorName == colorName }
        lines = if (existing == null) {
            lines + CartLine(productId, colorName, quantity = 1)
        } else {
            lines.map { line ->
                if (line.key == existing.key) {
                    line.copy(quantity = (line.quantity + 1).coerceAtMost(MAX_QUANTITY))
                } else {
                    line
                }
            }
        }
    }

    fun increment(key: String) {
        lines = lines.map { line ->
            if (line.key == key) line.copy(quantity = (line.quantity + 1).coerceAtMost(MAX_QUANTITY)) else line
        }
    }

    fun decrement(key: String) {
        lines = lines.mapNotNull { line ->
            if (line.key != key) return@mapNotNull line
            if (line.quantity <= 1) null else line.copy(quantity = line.quantity - 1)
        }
    }

    fun toggleWishlist(productId: String) {
        wishlist = if (productId in wishlist) wishlist - productId else wishlist + productId
    }

    fun selectAddress(id: String) {
        selectedAddressId = id
    }

    fun applyCoupon(code: String) {
        appliedCoupon = if (appliedCoupon == code) null else code
    }

    fun markNotificationRead(id: String) {
        notifications = notifications.map { note ->
            if (note.id == id) note.copy(read = true) else note
        }
    }

    fun order(id: String): ShopOrder? = orders.find { it.id == id }

    fun checkout() {
        if (lines.isEmpty()) return
        val address = selectedAddress
        val order = SampleAccount.order(
            id = "PC${1843 + orders.size}",
            lines = lines,
            total = total,
            stage = TrackingStage.Placed,
            eta = "Arriving tomorrow",
            placedOn = "Today",
            address = address?.let { "${it.label} · ${it.lines}" } ?: "Saved address",
            carrier = "Parcel Rush",
        )
        orders = listOf(order) + orders
        lastOrder = order
        lines = emptyList()
        appliedCoupon = null
    }

    private companion object {
        const val MAX_QUANTITY = 9
    }
}
