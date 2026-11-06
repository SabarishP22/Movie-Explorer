package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ShopNavHost(
    cartViewModel: CartViewModel,
    flyState: FlyToCartState,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val showBar = route in Routes.tabs

    SharedTransitionLayout(modifier = modifier) {
        val sharedScope = this
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable(Routes.HOME) {
                    HomeScreen(
                        sharedTransitionScope = sharedScope,
                        animatedVisibilityScope = this,
                        onOpenSearch = { navController.navigate(Routes.SEARCH) },
                        onOpenProduct = { id -> navController.navigate(Routes.product(id)) },
                    )
                }
                composable(Routes.CATEGORIES) {
                    CategoriesScreen(
                        onOpenProduct = { id -> navController.navigate(Routes.product(id)) },
                    )
                }
                composable(Routes.SEARCH) {
                    SearchScreen(
                        sharedTransitionScope = sharedScope,
                        animatedVisibilityScope = this,
                        onBack = { navController.popBackStack() },
                        onOpenProduct = { id -> navController.navigate(Routes.product(id)) },
                    )
                }
                composable(
                    route = Routes.PRODUCT,
                    arguments = listOf(
                        navArgument(Routes.PRODUCT_ID) { type = NavType.StringType },
                    ),
                    enterTransition = { fadeIn(tween(280)) },
                    exitTransition = { fadeOut(tween(200)) },
                    popEnterTransition = { fadeIn(tween(220)) },
                    popExitTransition = { fadeOut(tween(200)) },
                ) { entry ->
                    val productId = entry.arguments?.getString(Routes.PRODUCT_ID).orEmpty()
                    val product = SampleCatalog.find(productId)
                    if (product == null) {
                        LaunchedEffect(productId) { navController.popBackStack() }
                    } else {
                        ProductScreen(
                            product = product,
                            cartCount = cartViewModel.itemCount,
                            saved = product.id in cartViewModel.wishlist,
                            sharedTransitionScope = sharedScope,
                            animatedVisibilityScope = this,
                            onToggleSaved = { cartViewModel.toggleWishlist(product.id) },
                            onBack = { navController.popBackStack() },
                            onOpenCart = { navController.openTab(Routes.CART) },
                            onCartAnchor = { flyState.target = it },
                            onAddToCart = { colorName, origin ->
                                cartViewModel.add(product.id, colorName)
                                flyState.launch(origin, product)
                            },
                        )
                    }
                }
                composable(Routes.CART) {
                    val address = cartViewModel.selectedAddress
                    CartScreen(
                        lines = cartViewModel.lines,
                        merchandise = cartViewModel.merchandiseTotal,
                        discount = cartViewModel.discount,
                        total = cartViewModel.total,
                        addressLabel = address?.let { "${it.label} · ${it.lines}" } ?: "Choose an address",
                        onChangeAddress = { navController.navigate(Routes.ADDRESSES) },
                        onOpenCoupons = { navController.navigate(Routes.COUPONS) },
                        onIncrement = cartViewModel::increment,
                        onDecrement = cartViewModel::decrement,
                        onCheckout = {
                            cartViewModel.checkout()
                            navController.navigate(Routes.CONFIRMED)
                        },
                    )
                }
                composable(Routes.ORDERS) {
                    OrdersScreen(
                        orders = cartViewModel.orders,
                        onOpenOrder = { id -> navController.navigate(Routes.tracking(id)) },
                    )
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        profile = cartViewModel.profile,
                        unread = cartViewModel.unreadCount,
                        wishlistCount = cartViewModel.wishlist.size,
                        onOrders = { navController.openTab(Routes.ORDERS) },
                        onWishlist = { navController.navigate(Routes.WISHLIST) },
                        onAddresses = { navController.navigate(Routes.ADDRESSES) },
                        onPayments = { navController.navigate(Routes.PAYMENTS) },
                        onCoupons = { navController.navigate(Routes.COUPONS) },
                        onNotifications = { navController.navigate(Routes.NOTIFICATIONS) },
                    )
                }
                composable(
                    route = Routes.TRACKING,
                    arguments = listOf(
                        navArgument(Routes.ORDER_ID) { type = NavType.StringType },
                    ),
                ) { entry ->
                    val orderId = entry.arguments?.getString(Routes.ORDER_ID).orEmpty()
                    val order = cartViewModel.order(orderId)
                    if (order == null) {
                        LaunchedEffect(orderId) { navController.popBackStack() }
                    } else {
                        TrackingScreen(
                            order = order,
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
                composable(Routes.WISHLIST) {
                    WishlistScreen(
                        ids = cartViewModel.wishlist,
                        onBack = { navController.popBackStack() },
                        onOpenProduct = { id -> navController.navigate(Routes.product(id)) },
                        onRemove = cartViewModel::toggleWishlist,
                    )
                }
                composable(Routes.ADDRESSES) {
                    AddressesScreen(
                        addresses = cartViewModel.addresses,
                        selectedId = cartViewModel.selectedAddressId,
                        onSelect = cartViewModel::selectAddress,
                        onBack = { navController.popBackStack() },
                    )
                }
                composable(Routes.PAYMENTS) {
                    PaymentsScreen(
                        cards = cartViewModel.payments,
                        onBack = { navController.popBackStack() },
                    )
                }
                composable(Routes.COUPONS) {
                    CouponsScreen(
                        coupons = cartViewModel.coupons,
                        applied = cartViewModel.appliedCoupon,
                        onApply = cartViewModel::applyCoupon,
                        onBack = { navController.popBackStack() },
                    )
                }
                composable(Routes.NOTIFICATIONS) {
                    NotificationsScreen(
                        notes = cartViewModel.notifications,
                        onOpen = cartViewModel::markNotificationRead,
                        onBack = { navController.popBackStack() },
                    )
                }
                composable(
                    route = Routes.CONFIRMED,
                    enterTransition = { fadeIn(tween(280)) },
                    popExitTransition = { fadeOut(tween(200)) },
                ) {
                    OrderConfirmedScreen(
                        order = cartViewModel.lastOrder,
                        onTrack = {
                            cartViewModel.lastOrder?.let { order ->
                                navController.navigate(Routes.tracking(order.id))
                            }
                        },
                        onOrders = { navController.openTab(Routes.ORDERS) },
                        onContinue = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        },
                    )
                }
            }
            AnimatedVisibility(
                visible = showBar,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                ParcelDock(
                    current = route,
                    itemCount = cartViewModel.itemCount,
                    unreadCount = cartViewModel.unreadCount,
                    onCartAnchor = { anchor ->
                        if (showBar) flyState.target = anchor
                    },
                    onHome = { navController.openTab(Routes.HOME) },
                    onBrowse = { navController.openTab(Routes.CATEGORIES) },
                    onCart = { navController.openTab(Routes.CART) },
                    onOrders = { navController.openTab(Routes.ORDERS) },
                    onYou = { navController.openTab(Routes.PROFILE) },
                )
            }
        }
    }
}

private fun NavHostController.openTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val CATEGORIES = "categories"
    const val CART = "cart"
    const val ORDERS = "orders"
    const val PROFILE = "profile"
    const val CONFIRMED = "confirmed"
    const val WISHLIST = "wishlist"
    const val ADDRESSES = "addresses"
    const val PAYMENTS = "payments"
    const val COUPONS = "coupons"
    const val NOTIFICATIONS = "notifications"
    const val PRODUCT_ID = "productId"
    const val ORDER_ID = "orderId"
    const val PRODUCT = "product/{$PRODUCT_ID}"
    const val TRACKING = "tracking/{$ORDER_ID}"

    val tabs = setOf(HOME, CATEGORIES, CART, ORDERS, PROFILE)

    fun product(id: String): String = "product/$id"

    fun tracking(id: String): String = "tracking/$id"
}
