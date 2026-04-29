package ni.edu.uam.appdecafeteria.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.appdecafeteria.ui.CoffeeViewModel
import ni.edu.uam.appdecafeteria.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    products: List<Product>,
    onProductClick: (Int) -> Unit,
    viewModel: CoffeeViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("Todo") }
    var isSearchActive by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    
    val categories = listOf("Todo", "Café", "Matcha", "Pastelería", "Bebidas")

    Scaffold(
        topBar = {
            HomeHeader(
                isSearchActive = isSearchActive,
                onSearchToggle = { isSearchActive = !isSearchActive },
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Selector de Categorías con Animación
            AnimatedVisibility(
                visible = !isSearchActive,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CategorySelector(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Título dinámico
                item {
                    Text(
                        text = when {
                            isSearchActive && uiState.searchQuery.isNotEmpty() -> "Resultados para \"${uiState.searchQuery}\""
                            selectedCategory == "Todo" -> "Nuestros Favoritos"
                            else -> "Especialidades de $selectedCategory"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Filtrado Lógico (Categoría + Búsqueda)
                val filteredProducts = products.filter { product ->
                    (selectedCategory == "Todo" || product.category == selectedCategory) &&
                    (product.name.contains(uiState.searchQuery, ignoreCase = true) || 
                     product.description.contains(uiState.searchQuery, ignoreCase = true))
                }

                items(filteredProducts, key = { it.id }) { product ->
                    AnimatedProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
                
                if (filteredProducts.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text("No encontramos lo que buscas ☕", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader(
    isSearchActive: Boolean,
    onSearchToggle: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (!isSearchActive) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Aromas de Aquí",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "CAFETERÍA & PASTELERÍA",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    letterSpacing = 2.sp
                )
            }
        } else {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Busca tu café favorito...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }

        IconButton(
            onClick = onSearchToggle,
            modifier = Modifier
                .background(
                    if (isSearchActive) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    CircleShape
                )
        ) {
            Icon(
                imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (!isSearchActive) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("☕", fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                    labelColor = MaterialTheme.colorScheme.primary
                ),
                border = null,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun AnimatedProductCard(product: Product, onClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Imagen con icono distintivo por categoría
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(130.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when(product.category) {
                            "Café" -> "☕"
                            "Matcha" -> "🍵"
                            "Pastelería" -> "🍰"
                            "Bebidas" -> "🍹"
                            else -> "✨"
                        },
                        fontSize = 48.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                                Text(" 4.9", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$${product.basePrice}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
