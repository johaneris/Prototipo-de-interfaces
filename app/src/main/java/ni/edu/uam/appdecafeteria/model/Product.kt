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
    val imageRes: Int, // En una app real usaríamos URLs, aquí IDs de recursos
    val category: String,
    val availableIngredients: List<Ingredient> = emptyList()
) {
    val totalPrice: Double
        get() = basePrice + availableIngredients.filter { it.isSelected }.sumOf { it.price }
}
