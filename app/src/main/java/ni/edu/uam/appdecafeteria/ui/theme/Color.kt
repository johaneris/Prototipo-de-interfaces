package ni.edu.uam.appdecafeteria.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta basada en la imagen (tonos café, crema y oscuro)
val CoffeeBrown = Color(0xFF634832) // Café oscuro del logo
val CoffeeCream = Color(0xFFF5E6D3) // Fondo crema claro del logo (ajustado para ser más claro)
val CoffeeBeige = Color(0xFFD7C0A7) // Tono beige intermedio
val CoffeeDark = Color(0xFF2B1B17)  // Tono café casi negro para textos
val CoffeeAccent = Color(0xFF8D6E63) // Café suave para acentos

// Material 3 Color Scheme mapping
val Primary = CoffeeBrown
val OnPrimary = Color.White
val PrimaryContainer = CoffeeBeige
val OnPrimaryContainer = CoffeeDark

val Secondary = CoffeeAccent
val OnSecondary = Color.White
val SecondaryContainer = CoffeeCream
val OnSecondaryContainer = CoffeeDark

val Background = Color(0xFFFDF8F5) // Un blanco hueso muy suave
val OnBackground = CoffeeDark
val Surface = Color.White
val OnSurface = CoffeeDark
