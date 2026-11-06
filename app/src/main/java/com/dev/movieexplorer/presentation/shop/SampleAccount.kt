package com.dev.movieexplorer.presentation.shop

import androidx.compose.ui.graphics.Color

object SampleAccount {
    val profile = ShopProfile(
        name = "Asha Menon",
        email = "asha.menon@email.com",
        phone = "+91 98450 22118",
        membership = "Parcel Plus",
        memberSince = "Member since 2022",
    )

    val addresses = listOf(
        SavedAddress(
            id = "home",
            label = "Home",
            person = "Asha Menon",
            lines = "14, 4th Cross, Indiranagar, Bengaluru 560038",
            phone = "+91 98450 22118",
        ),
        SavedAddress(
            id = "work",
            label = "Work",
            person = "Asha Menon",
            lines = "Tower B, EPIP Zone, Whitefield, Bengaluru 560066",
            phone = "+91 98450 22118",
        ),
        SavedAddress(
            id = "parents",
            label = "Parents",
            person = "Lakshmi Menon",
            lines = "221, Marine Drive, Kochi 682031",
            phone = "+91 98470 11820",
        ),
    )

    val payments = listOf(
        SavedCard("visa", "Visa", "4242", "08/28", Color(0xFF1A3A6B)),
        SavedCard("master", "Mastercard", "5512", "02/27", Color(0xFF5C2E2E)),
        SavedCard("upi", "UPI", "asha@okaxis", "Always on", Color(0xFF1F4D42)),
    )

    val coupons = listOf(
        Coupon("PARCEL10", "₹10 off", "Any order today", 10.0),
        Coupon("PLUS15", "₹15 off", "Parcel Plus members", 15.0),
        Coupon("HOME50", "₹50 off home", "Home category, one use", 50.0),
    )

    val notifications = listOf(
        ShopNotification(
            "n1",
            "Out for delivery",
            "Studio headphones reach Indiranagar today.",
            "2h ago",
            read = false,
        ),
        ShopNotification(
            "n2",
            "Price drop",
            "Arc desk lamp is ₹12 less than when you saved it.",
            "Yesterday",
            read = false,
        ),
        ShopNotification(
            "n3",
            "Coupon expiring",
            "HOME50 ends Sunday night.",
            "2 days ago",
            read = true,
        ),
        ShopNotification(
            "n4",
            "Parcel Plus",
            "Your free delivery renews on 1 Dec.",
            "Last week",
            read = true,
        ),
    )

    val wishlist = setOf("lamp", "bottle")

    val orders: List<ShopOrder> = listOf(
        order(
            id = "PC1842",
            lines = listOf(CartLine("headphones", "Graphite", 1)),
            total = 189.0,
            stage = TrackingStage.OutForDelivery,
            eta = "Arriving today by 8 PM",
            placedOn = "Today, 9:12 AM",
            address = "Home · 14, 4th Cross, Indiranagar",
            carrier = "Parcel Rush",
        ),
        order(
            id = "PC1790",
            lines = listOf(
                CartLine("skillet", "Iron", 1),
                CartLine("throw", "Rust", 1),
            ),
            total = 112.0,
            stage = TrackingStage.Delivered,
            eta = "Delivered 28 Oct",
            placedOn = "26 Oct",
            address = "Work · Tower B, Whitefield",
            carrier = "BlueDart",
        ),
        order(
            id = "PC1711",
            lines = listOf(CartLine("runners", "Ember", 1)),
            total = 128.0,
            stage = TrackingStage.Shipped,
            eta = "Arriving Monday",
            placedOn = "4 Nov",
            address = "Home · 14, 4th Cross, Indiranagar",
            carrier = "Parcel Rush",
        ),
    )

    fun order(
        id: String,
        lines: List<CartLine>,
        total: Double,
        stage: TrackingStage,
        eta: String,
        placedOn: String,
        address: String,
        carrier: String,
    ): ShopOrder {
        return ShopOrder(
            id = id,
            lines = lines,
            total = total,
            stage = stage,
            eta = eta,
            placedOn = placedOn,
            address = address,
            carrier = carrier,
            events = eventsFor(stage),
        )
    }

    fun eventsFor(stage: TrackingStage): List<TrackingEvent> {
        val all = listOf(
            TrackingEvent("Order placed", "Parcel received the order", "9:12 AM"),
            TrackingEvent("Packed", "Left the Bengaluru hub", "11:40 AM"),
            TrackingEvent("Shipped", "Handed to $stage", "2:05 PM"),
            TrackingEvent("Out for delivery", "The rider is in your area", "Today"),
            TrackingEvent("Delivered", "Handed over at the door", "Today"),
        )
        return all.take(stage.ordinal + 1).mapIndexed { index, event ->
            if (index == 2) event.copy(detail = "In transit with the carrier") else event
        }
    }
}
