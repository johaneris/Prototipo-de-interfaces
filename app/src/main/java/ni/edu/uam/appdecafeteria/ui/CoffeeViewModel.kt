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

        // Usamos URLs de Unsplash para que se vea profesional y con imágenes reales
        val mockProducts = listOf(
            Product(1, "Cappuccino Italiano", "Espresso intenso con leche cremosa y canela.", 120.0, 0, "Café", coffeeIngredients, "https://images.unsplash.com/photo-1534778101976-62847782c213?q=80&w=500"),
            Product(2, "Latte de Vainilla", "Suave combinación de espresso y vainilla francesa.", 140.0, 0, "Café", coffeeIngredients, "https://images.unsplash.com/photo-1570968015849-0497e063877a?q=80&w=500"),
            Product(3, "Americano Reserva", "Granos seleccionados para un sabor profundo.", 80.0, 0, "Café", imageUrl = "https://images.unsplash.com/photo-1551033406-611cf9a28f67?q=80&w=500"),
            Product(4, "Matcha Ceremonial", "Té verde orgánico de grado ceremonial.", 160.0, 0, "Matcha", coffeeIngredients, "https://images.unsplash.com/photo-1515823064-d6e0c04616a7?q=80&w=500"),
            Product(5, "Iced Matcha Mint", "Refrescante con menta fresca y hielo.", 150.0, 0, "Matcha", imageUrl = "https://images.unsplash.com/photo-1593444209118-205ee99607f0?q=80&w=500"),
            Product(6, "Croissant de Almendras", "Hojaldre artesanal con crema de almendras.", 95.0, 0, "Pastelería", imageUrl = "https://images.unsplash.com/photo-1555507036-ab1f4038808a?q=80&w=500"),
            Product(7, "Cheesecake de Frutos", "Queso crema premium con frutos rojos.", 180.0, 0, "Pastelería", imageUrl = "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?q=80&w=500"),
            Product(8, "Frappé Caramelo", "Dulce sensación helada con whipped cream.", 155.0, 0, "Bebidas", imageUrl = "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?q=80&w=500"),
            Product(9, "Smoothie Tropical", "Mezcla de mango, piña y pasión.", 130.0, 0, "Bebidas", imageUrl = "https://images.unsplash.com/photo-1502741224143-90386d7f8c82?q=80&w=500")
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

    fun incrementQuantity(cartItem: CartItem) {
        _uiState.update { currentState ->
            currentState.copy(cart = currentState.cart.map {
                if (it == cartItem) it.copy(quantity = it.quantity + 1) else it
            })
        }
    }

    fun decrementQuantity(cartItem: CartItem) {
        _uiState.update { currentState ->
            if (cartItem.quantity > 1) {
                currentState.copy(cart = currentState.cart.map {
                    if (it == cartItem) it.copy(quantity = it.quantity - 1) else it
                })
            } else {
                currentState.copy(cart = currentState.cart - cartItem)
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
