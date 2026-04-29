package ni.edu.uam.appdecafeteria.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta Premium & Moderna: "Aromas de Aquí"
// Inspirada en cafeterías de especialidad minimalistas
val CoffeeDark = Color(0xFF1C1412)      // Ébano profundo para textos y elementos clave
val CoffeeBrown = Color(0xFF4E342E)     // Marrón moca rico (Primario)
val CoffeeWarm = Color(0xFF8D6E63)      // Sombra cálida para elementos secundarios
val CoffeeGold = Color(0xFFC6A664)      // Dorado tostado para acentos y estados premium
val CoffeeLatte = Color(0xFFEFEBE9)     // Crema latte muy claro para superficies y fondos
val CoffeeWhite = Color(0xFFFAF9F6)     // Blanco roto para el fondo general
val CoffeeOrange = Color(0xFFD87D4A)    // Naranja quemado para botones de acción (tipo PedidosYa)

// Mapeo a Material 3
val Primary = CoffeeBrown
val OnPrimary = Color.White
val PrimaryContainer = CoffeeLatte
val OnPrimaryContainer = CoffeeBrown

val Secondary = CoffeeWarm
val OnSecondary = Color.White
val SecondaryContainer = Color(0xFFF5F0EE)
val OnSecondaryContainer = CoffeeDark

val Accent = CoffeeOrange
val Background = CoffeeWhite
val OnBackground = CoffeeDark
val Surface = Color.White
val OnSurface = CoffeeDark

val ErrorRed = Color(0xFFB00020)
val SuccessGreen = Color(0xFF388E3C)
