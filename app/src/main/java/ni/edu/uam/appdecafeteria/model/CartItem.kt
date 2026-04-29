package ni.edu.uam.appdecafeteria.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1
) {
    val totalPrice: Double
        get() = product.totalPrice * quantity
}
