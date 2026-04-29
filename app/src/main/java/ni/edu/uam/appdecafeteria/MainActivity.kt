package ni.edu.uam.appdecafeteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ni.edu.uam.appdecafeteria.ui.CoffeeApp
import ni.edu.uam.appdecafeteria.ui.theme.AppDeCafeteriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppDeCafeteriaTheme {
                CoffeeApp()
            }
        }
    }
}
