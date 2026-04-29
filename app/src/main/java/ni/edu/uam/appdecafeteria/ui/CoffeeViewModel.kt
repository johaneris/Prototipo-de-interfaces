package ni.edu.uam.appdecafeteria.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.appdecafeteria.model.CartItem
import ni.edu.uam.appdecafeteria.model.Ingredient
import ni.edu.uam.appdecafeteria.model.Product

data class CoffeeUiState(
    val products: List<Product> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val userName: String = "Johaneris Sayrin Avalos",
    val userEmail: String = "johaneris.avalos@uam.edu.ni",
    val userAddress: String = "Rotonda Universitaria 1km al Sur, Managua",
    val userProfileImage: Uri? = null,
    val orderStatus: String = "No hay pedidos activos",
    val searchQuery: String = "",
    val isLoggedIn: Boolean = true
)

class CoffeeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CoffeeUiState())
    val uiState: StateFlow<CoffeeUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val coffeeIngredients = listOf(
            Ingredient("Leche de Almendras", 20.0),
            Ingredient("Extra Shot Espresso", 30.0),
            Ingredient("Caramelo", 15.0),
            Ingredient("Crema Batida", 25.0)
        )

        val mockProducts = listOf(
            Product(1, "Cappuccino", "Café espresso con leche vaporizada y espuma.", 120.0, 0, "Café", coffeeIngredients),
            Product(2, "Latte Macchiato", "Leche manchada con un toque de café espresso.", 140.0, 0, "Café", coffeeIngredients),
            Product(3, "Americano", "Espresso con agua caliente para un sabor suave.", 80.0, 0, "Café"),
            Product(4, "Matcha Latte", "Té verde matcha premium con leche cremosa.", 160.0, 0, "Matcha", coffeeIngredients),
            Product(5, "Iced Matcha", "Matcha refrescante con hielo y un toque de vainilla.", 150.0, 0, "Matcha"),
            Product(6, "Croissant de Mantequilla", "Hojaldre crujiente y tierno de pura mantequilla.", 95.0, 0, "Pastelería"),
            Product(7, "Cheesecake de Fresa", "Tarta de queso cremosa con coulis de fresas naturales.", 180.0, 0, "Pastelería"),
            Product(8, "Frappé de Caramelo", "Bebida fría granizada con jarabe de caramelo.", 155.0, 0, "Bebidas"),
            Product(9, "Té Helado de Melocotón", "Té negro refrescante con sabor a melocotón.", 70.0, 0, "Bebidas")
        )
        _uiState.update { it.copy(products = mockProducts) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addToCart(product: Product) {
        _uiState.update { currentState ->
            val existingItem = currentState.cart.find { it.product.id == product.id && it.product.availableIngredients == product.availableIngredients }
            if (existingItem != null) {
                currentState.copy(cart = currentState.cart.map {
                    if (it == existingItem) it.copy(quantity = it.quantity + 1) else it
                })
            } else {
                currentState.copy(cart = currentState.cart + CartItem(product.copy()))
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        _uiState.update { currentState ->
            currentState.copy(cart = currentState.cart - cartItem)
        }
    }
    
    fun clearCart() {
        _uiState.update { it.copy(cart = emptyList(), orderStatus = "Pedido en preparación...") }
    }

    fun updateProfile(name: String, email: String, address: String, imageUri: Uri?) {
        _uiState.update { it.copy(
            userName = name,
            userEmail = email,
            userAddress = address,
            userProfileImage = imageUri
        ) }
    }

    fun logout() {
        _uiState.update { it.copy(isLoggedIn = false) }
    }
}
