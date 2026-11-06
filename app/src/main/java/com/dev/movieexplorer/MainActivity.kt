package com.dev.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.movieexplorer.presentation.shop.CartViewModel
import com.dev.movieexplorer.presentation.shop.FlyToCartOverlay
import com.dev.movieexplorer.presentation.shop.FlyToCartState
import com.dev.movieexplorer.presentation.shop.ShopNavHost
import com.dev.movieexplorer.ui.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cartViewModel: CartViewModel = viewModel()
            val flyState = remember { FlyToCartState() }
            MovieExplorerTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    ShopNavHost(
                        cartViewModel = cartViewModel,
                        flyState = flyState,
                        modifier = Modifier.fillMaxSize(),
                    )
                    FlyToCartOverlay(
                        state = flyState,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
