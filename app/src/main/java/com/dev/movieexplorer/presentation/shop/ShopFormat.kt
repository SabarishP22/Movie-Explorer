package com.dev.movieexplorer.presentation.shop

import java.text.NumberFormat
import java.util.Locale

private val currency: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
private val integers: NumberFormat = NumberFormat.getIntegerInstance(Locale.US)

fun formatPrice(amount: Double): String = currency.format(amount)

fun formatCount(count: Int): String = integers.format(count)

fun sharedImageKey(productId: String): String = "product-image-$productId"
