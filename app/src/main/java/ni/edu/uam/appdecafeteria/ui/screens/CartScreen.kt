package ni.edu.uam.appdecafeteria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ni.edu.uam.appdecafeteria.model.CartItem

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    onRemoveItem: (CartItem) -> Unit,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tu Carrito",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(text = "Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemRow(item = item, onRemove = { onRemoveItem(item) })
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            val total = cartItems.sumOf { it.totalPrice }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total a pagar:", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "$${String.format("%.2f", total)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Proceder al Pago")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            item.product.availableIngredients.filter { it.isSelected }.forEach {
                Text(text = "+ ${it.name}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "Cantidad: ${item.quantity} - $${String.format("%.2f", item.totalPrice)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }
    }
}
