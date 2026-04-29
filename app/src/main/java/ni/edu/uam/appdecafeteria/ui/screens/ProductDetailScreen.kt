package ni.edu.uam.appdecafeteria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Imagen del Producto con AsyncImage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Degradado para que el texto o botones superiores resalten si es necesario
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent),
                                startY = 0f,
                                endY = 200f
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = product.category,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (currentProduct.availableIngredients.isNotEmpty()) {
                    Text(
                        text = "Extras & Personalización",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(currentProduct.availableIngredients.indices.toList()) { index ->
                            val ingredient = currentProduct.availableIngredients[index]
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = if (ingredient.isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color(0xFFF8F8F8),
                                border = if (ingredient.isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) else null,
                                onClick = {
                                    val newList = currentProduct.availableIngredients.toMutableList()
                                    newList[index] = ingredient.copy(isSelected = !ingredient.isSelected)
                                    currentProduct = currentProduct.copy(availableIngredients = newList)
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = ingredient.name, 
                                            fontWeight = FontWeight.Bold,
                                            color = if (ingredient.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "+$${String.format(Locale.US, "%.2f", ingredient.price)}", 
                                            style = MaterialTheme.typography.bodySmall, 
                                            color = Color.Gray
                                        )
                                    }
                                    Checkbox(
                                        checked = ingredient.isSelected,
                                        onCheckedChange = { isChecked ->
                                            val newList = currentProduct.availableIngredients.toMutableList()
                                            newList[index] = ingredient.copy(isSelected = isChecked)
                                            currentProduct = currentProduct.copy(availableIngredients = newList)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text("Total", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", currentProduct.totalPrice)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Button(
                            onClick = { onAddToCart(currentProduct) },
                            modifier = Modifier.height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD87D4A))
                        ) {
                            Text("Añadir al Carrito", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
