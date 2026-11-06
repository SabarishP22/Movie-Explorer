package com.dev.movieexplorer.presentation.shop

import androidx.compose.ui.graphics.Color

object SampleCatalog {
    const val ALL = "All"

    val categories: List<String> = listOf(ALL, "Electronics", "Home", "Style", "Outdoors")

    val products: List<Product> = listOf(
        product(
            id = "headphones",
            title = "Studio headphones",
            price = 189.0,
            rating = 4.7,
            reviews = 2403,
            category = "Electronics",
            seed = "parcel-headphones",
            fallback = Color(0xFF2C3338),
            badge = "Deal",
            blurb = "Soft ear cushions and a quiet room, wherever you sit.",
            swatches = listOf("Graphite" to Color(0xFF2C3338), "Sand" to Color(0xFFCDB79A)),
        ),
        product(
            id = "kettle",
            title = "Pour-over kettle",
            price = 64.0,
            rating = 4.8,
            reviews = 890,
            category = "Home",
            seed = "parcel-kettle",
            fallback = Color(0xFF8C9A8A),
            badge = null,
            blurb = "A long spout for a slow, even pour.",
            swatches = listOf("Sage" to Color(0xFF8C9A8A), "Cream" to Color(0xFFE7DCC8)),
        ),
        product(
            id = "overshirt",
            title = "Linen overshirt",
            price = 78.0,
            rating = 4.5,
            reviews = 412,
            category = "Style",
            seed = "parcel-linen",
            fallback = Color(0xFFC4A484),
            badge = "Best seller",
            blurb = "An easy layer that gets softer every wash.",
            swatches = listOf("Oat" to Color(0xFFC4A484), "Ink" to Color(0xFF243038), "Olive" to Color(0xFF5E6848)),
        ),
        product(
            id = "runners",
            title = "Trail runners",
            price = 128.0,
            rating = 4.6,
            reviews = 1560,
            category = "Outdoors",
            seed = "parcel-runners",
            fallback = Color(0xFFD06A3A),
            badge = "Deal",
            blurb = "Light on the road, grippy when the path turns.",
            swatches = listOf("Ember" to Color(0xFFD06A3A), "Night" to Color(0xFF1E2430)),
        ),
        product(
            id = "keyboard",
            title = "Mechanical keyboard",
            price = 149.0,
            rating = 4.4,
            reviews = 733,
            category = "Electronics",
            seed = "parcel-keyboard",
            fallback = Color(0xFF3E4654),
            badge = null,
            blurb = "Quiet switches and a deck that stays put.",
            swatches = listOf("Slate" to Color(0xFF3E4654), "Fog" to Color(0xFFD5D8DE)),
        ),
        product(
            id = "lamp",
            title = "Arc desk lamp",
            price = 92.0,
            rating = 4.9,
            reviews = 1204,
            category = "Home",
            seed = "parcel-lamp",
            fallback = Color(0xFFE2B15A),
            badge = "Deal",
            blurb = "Warm light that bends out of the way.",
            swatches = listOf("Brass" to Color(0xFFE2B15A), "Black" to Color(0xFF1A1A1A)),
        ),
        product(
            id = "throw",
            title = "Wool throw",
            price = 58.0,
            rating = 4.7,
            reviews = 640,
            category = "Home",
            seed = "parcel-throw",
            fallback = Color(0xFF8E4B3A),
            badge = null,
            blurb = "A heavy weave for the end of the couch.",
            swatches = listOf("Rust" to Color(0xFF8E4B3A), "Stone" to Color(0xFFB7A99A)),
        ),
        product(
            id = "weekender",
            title = "Canvas weekender",
            price = 118.0,
            rating = 4.6,
            reviews = 388,
            category = "Style",
            seed = "parcel-bag",
            fallback = Color(0xFF6B5344),
            badge = null,
            blurb = "One bag for a Friday that turns into Sunday.",
            swatches = listOf("Tan" to Color(0xFF6B5344), "Navy" to Color(0xFF1C2A44)),
        ),
        product(
            id = "drone",
            title = "Pocket drone",
            price = 249.0,
            rating = 4.3,
            reviews = 990,
            category = "Electronics",
            seed = "parcel-drone",
            fallback = Color(0xFF7E8C9A),
            badge = "Best seller",
            blurb = "Folds flat and keeps the horizon steady.",
            swatches = listOf("Silver" to Color(0xFF7E8C9A), "Orange" to Color(0xFFE07A2F)),
        ),
        product(
            id = "skillet",
            title = "Cast iron skillet",
            price = 54.0,
            rating = 4.8,
            reviews = 2100,
            category = "Home",
            seed = "parcel-skillet",
            fallback = Color(0xFF3F3A36),
            badge = null,
            blurb = "Seasoned and ready for a high flame.",
            swatches = listOf("Iron" to Color(0xFF3F3A36)),
        ),
        product(
            id = "shades",
            title = "Round sunglasses",
            price = 42.0,
            rating = 4.4,
            reviews = 275,
            category = "Style",
            seed = "parcel-shades",
            fallback = Color(0xFF1B1B1B),
            badge = null,
            blurb = "A light frame with lenses that don't glare.",
            swatches = listOf("Black" to Color(0xFF1B1B1B), "Tortoise" to Color(0xFF7A4E2D)),
        ),
        product(
            id = "bottle",
            title = "Insulated bottle",
            price = 34.0,
            rating = 4.9,
            reviews = 3400,
            category = "Outdoors",
            seed = "parcel-bottle",
            fallback = Color(0xFF2F6F62),
            badge = "Deal",
            blurb = "Ice in the morning is still ice after lunch.",
            swatches = listOf("Sea" to Color(0xFF2F6F62), "Clay" to Color(0xFFC46A4A), "White" to Color(0xFFF4F1EA)),
        ),
    )

    val deals: List<Product> = products.filter { it.badge != null }

    fun find(id: String): Product? = products.find { it.id == id }

    fun inCategory(category: String): List<Product> {
        if (category == ALL) return products
        return products.filter { it.category == category }
    }

    fun search(query: String): List<Product> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return products
        return products.filter { product ->
            product.title.contains(trimmed, ignoreCase = true) ||
                product.category.contains(trimmed, ignoreCase = true) ||
                product.blurb.contains(trimmed, ignoreCase = true)
        }
    }

    private fun product(
        id: String,
        title: String,
        price: Double,
        rating: Double,
        reviews: Int,
        category: String,
        seed: String,
        fallback: Color,
        badge: String?,
        blurb: String,
        swatches: List<Pair<String, Color>>,
    ): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            rating = rating,
            reviewCount = reviews,
            category = category,
            imageUrl = "https://picsum.photos/seed/$seed/800/800",
            fallback = fallback,
            colors = swatches.map { (name, color) -> Swatch(name, color) },
            badge = badge,
            blurb = blurb,
        )
    }
}
