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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
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
    val showBar = route == Routes.HOME || route == Routes.CART

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
                            sharedTransitionScope = sharedScope,
                            animatedVisibilityScope = this,
                            onBack = { navController.popBackStack() },
                            onOpenCart = { navController.navigate(Routes.CART) },
                            onCartAnchor = { flyState.target = it },
                            onAddToCart = { colorName, origin ->
                                cartViewModel.add(product.id, colorName)
                                flyState.launch(origin, product)
                            },
                        )
                    }
                }
                composable(Routes.CART) {
                    CartScreen(
                        lines = cartViewModel.lines,
                        total = cartViewModel.total,
                        onIncrement = cartViewModel::increment,
                        onDecrement = cartViewModel::decrement,
                        onCheckout = {
                            cartViewModel.checkout()
                            navController.navigate(Routes.CONFIRMED)
                        },
                    )
                }
                composable(
                    route = Routes.CONFIRMED,
                    enterTransition = { fadeIn(tween(280)) },
                    popExitTransition = { fadeOut(tween(200)) },
                ) {
                    OrderConfirmedScreen(
                        order = cartViewModel.lastOrder,
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
                ShopBottomBar(
                    current = route,
                    itemCount = cartViewModel.itemCount,
                    onCartAnchor = { anchor ->
                        if (showBar) flyState.target = anchor
                    },
                    onHome = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onCart = {
                        navController.navigate(Routes.CART) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ShopBottomBar(
    current: String?,
    itemCount: Int,
    onCartAnchor: (androidx.compose.ui.geometry.Offset) -> Unit,
    onHome: () -> Unit,
    onCart: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = current == Routes.HOME,
            onClick = onHome,
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("Home") },
        )
        NavigationBarItem(
            selected = current == Routes.CART,
            onClick = onCart,
            icon = {
                CartBadgeIcon(
                    count = itemCount,
                    onAnchor = onCartAnchor,
                )
            },
            label = { Text("Cart") },
        )
    }
}

private object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val CART = "cart"
    const val CONFIRMED = "confirmed"
    const val PRODUCT_ID = "productId"
    const val PRODUCT = "product/{$PRODUCT_ID}"

    fun product(id: String): String = "product/$id"
}
