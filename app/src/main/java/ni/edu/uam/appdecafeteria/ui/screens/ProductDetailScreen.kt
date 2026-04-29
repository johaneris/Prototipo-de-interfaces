package ni.edu.uam.appdecafeteria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.appdecafeteria.model.Product
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onBack: () -> Unit
) {
    var currentProduct by remember { mutableStateOf(product.copy()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Imagen representativa con Estilo Premium
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        Brush.verticalGradient(
                            listOf(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.primaryContainer)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when(product.category) {
                        "Café" -> "☕"
                        "Matcha" -> "🍵"
                        "Pastelería" -> "🥐"
                        else -> "🥤"
                    },
                    fontSize = 80.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (currentProduct.availableIngredients.isNotEmpty()) {
                Text(
                    text = "Personaliza tu pedido",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(currentProduct.availableIngredients.indices.toList()) { index ->
                        val ingredient = currentProduct.availableIngredients[index]
                        Card(
                            modifier = Modifier.padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(ingredient.name, fontWeight = FontWeight.SemiBold)
                                    Text("C$ ${String.format(Locale.getDefault(), "%.0f", ingredient.price)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                }
                                Checkbox(
                                    checked = ingredient.isSelected,
                                    onCheckedChange = { isChecked ->
                                        val newList = currentProduct.availableIngredients.toMutableList()
                                        newList[index] = ingredient.copy(isSelected = isChecked)
                                        currentProduct = currentProduct.copy(availableIngredients = newList)
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Precio Total", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text(
                        text = "C$ ${String.format(Locale.getDefault(), "%.0f", currentProduct.totalPrice)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Button(
                    onClick = { onAddToCart(currentProduct) },
                    modifier = Modifier.height(56.dp).padding(horizontal = 16.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Añadir al Carrito", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
