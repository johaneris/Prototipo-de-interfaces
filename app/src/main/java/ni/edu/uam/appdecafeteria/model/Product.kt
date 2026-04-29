package ni.edu.uam.appdecafeteria.model

data class Ingredient(
    val name: String,
    val price: Double,
    var isSelected: Boolean = false
)

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val basePrice: Double,
    val imageRes: Int,
    val category: String,
    val availableIngredients: List<Ingredient> = emptyList(),
    val imageUrl: String? = null
) {
    val totalPrice: Double
        get() = basePrice + availableIngredients.filter { it.isSelected }.sumOf { it.price }
}
