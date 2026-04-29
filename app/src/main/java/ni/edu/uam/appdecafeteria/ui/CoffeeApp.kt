package ni.edu.uam.appdecafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.appdecafeteria.ui.screens.*

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
    val uiState by viewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreenRoute = backStackEntry?.destination?.route ?: ""
    val currentScreen = when {
        currentScreenRoute.startsWith(CoffeeScreen.Home.name) -> CoffeeScreen.Home
        currentScreenRoute.startsWith(CoffeeScreen.Detail.name) -> CoffeeScreen.Detail
        currentScreenRoute.startsWith(CoffeeScreen.Cart.name) -> CoffeeScreen.Cart
        currentScreenRoute.startsWith(CoffeeScreen.Profile.name) -> CoffeeScreen.Profile
        currentScreenRoute.startsWith(CoffeeScreen.Payment.name) -> CoffeeScreen.Payment
        else -> CoffeeScreen.Home
    }

    if (!uiState.isLoggedIn) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Sesión Cerrada", 
                        style = MaterialTheme.typography.headlineMedium, 
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gracias por visitarnos. ¡Te esperamos pronto!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { /* En una app real, aquí reiniciamos el estado o vamos a Login */ },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Text("Volver a Entrar")
                    }
                }
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                if (currentScreen != CoffeeScreen.Detail && currentScreen != CoffeeScreen.Payment) {
                    CoffeeBottomBar(navController)
                }
            }
        ) { innerPadding ->
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
                        userAddress = uiState.userAddress,
                        userProfileImage = uiState.userProfileImage,
                        orderStatus = uiState.orderStatus,
                        onUpdateProfile = { name, email, address, imageUri ->
                            viewModel.updateProfile(name, email, address, imageUri)
                        },
                        onLogout = { viewModel.logout() }
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
