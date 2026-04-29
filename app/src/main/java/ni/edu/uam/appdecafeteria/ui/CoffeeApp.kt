package ni.edu.uam.appdecafeteria.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.appdecafeteria.ui.screens.CartScreen
import ni.edu.uam.appdecafeteria.ui.screens.HomeScreen
import ni.edu.uam.appdecafeteria.ui.screens.PaymentScreen
import ni.edu.uam.appdecafeteria.ui.screens.ProductDetailScreen
import ni.edu.uam.appdecafeteria.ui.screens.ProfileScreen

enum class CoffeeScreen(val title: String) {
    Home("Menú"),
    Detail("Detalle"),
    Cart("Carrito"),
    Profile("Perfil"),
    Payment("Pago")
}

@Composable
fun CoffeeApp(
    viewModel: CoffeeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CoffeeScreen.valueOf(
        backStackEntry?.destination?.route?.split("/")?.first() ?: CoffeeScreen.Home.name
    )

    Scaffold(
        bottomBar = {
            if (currentScreen != CoffeeScreen.Detail && currentScreen != CoffeeScreen.Payment) {
                CoffeeBottomBar(navController)
            }
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CoffeeScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = CoffeeScreen.Home.name) {
                HomeScreen(
                    products = uiState.products,
                    onProductClick = { productId ->
                        navController.navigate("${CoffeeScreen.Detail.name}/$productId")
                    }
                )
            }
            composable(route = "${CoffeeScreen.Detail.name}/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                val product = uiState.products.find { it.id == productId }
                if (product != null) {
                    ProductDetailScreen(
                        product = product,
                        onAddToCart = { customizedProduct ->
                            viewModel.addToCart(customizedProduct)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable(route = CoffeeScreen.Cart.name) {
                CartScreen(
                    cartItems = uiState.cart,
                    onRemoveItem = { viewModel.removeFromCart(it) },
                    onCheckout = { navController.navigate(CoffeeScreen.Payment.name) }
                )
            }
            composable(route = CoffeeScreen.Profile.name) {
                ProfileScreen(
                    userName = uiState.userName,
                    userEmail = uiState.userEmail,
                    orderStatus = uiState.orderStatus
                )
            }
            composable(route = CoffeeScreen.Payment.name) {
                PaymentScreen(
                    onPaymentSuccess = {
                        viewModel.clearCart()
                        navController.navigate(CoffeeScreen.Profile.name) {
                            popUpTo(CoffeeScreen.Home.name)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun CoffeeBottomBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        
        NavigationBarItem(
            selected = currentDestination?.route == CoffeeScreen.Home.name,
            onClick = { navController.navigate(CoffeeScreen.Home.name) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Menú") }
        )
        NavigationBarItem(
            selected = currentDestination?.route == CoffeeScreen.Cart.name,
            onClick = { navController.navigate(CoffeeScreen.Cart.name) },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            label = { Text("Carrito") }
        )
        NavigationBarItem(
            selected = currentDestination?.route == CoffeeScreen.Profile.name,
            onClick = { navController.navigate(CoffeeScreen.Profile.name) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Perfil") }
        )
    }
}
