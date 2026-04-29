package ni.edu.uam.appdecafeteria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información de Pago") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ingresa los detalles de tu tarjeta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 16) cardNumber = it },
                label = { Text("Número de Tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("1234 5678 1234 5678") }
            )

            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                label = { Text("Nombre en la Tarjeta") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { if (it.length <= 5) expiryDate = it },
                    label = { Text("MM/YY") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onPaymentSuccess,
                modifier = Modifier.fillMaxWidth(),
                enabled = cardNumber.length >= 13 && cardHolder.isNotEmpty() && expiryDate.length >= 4 && cvv.length == 3,
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Confirmar Pedido y Pagar")
            }
        }
    }
}
