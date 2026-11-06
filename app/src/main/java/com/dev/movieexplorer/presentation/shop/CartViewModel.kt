package com.dev.movieexplorer.presentation.shop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    var lines by mutableStateOf<List<CartLine>>(emptyList())
        private set

    var lastOrder by mutableStateOf<PlacedOrder?>(null)
        private set

    val itemCount: Int
        get() = lines.sumOf { it.quantity }

    val total: Double
        get() = lines.sumOf { line ->
            val price = SampleCatalog.find(line.productId)?.price ?: 0.0
            price * line.quantity
        }

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

    fun checkout() {
        if (lines.isEmpty()) return
        lastOrder = PlacedOrder(lines = lines, total = total)
        lines = emptyList()
    }

    private companion object {
        const val MAX_QUANTITY = 9
    }
}
