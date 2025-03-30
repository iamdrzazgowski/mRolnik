package com.example.mrolnik.screen


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController)
        }
        composable("animals") {
            AnimalsManagementScreen(navController)
        }
        composable("chat") {
            ChatScreen()
        }
        composable("fields") {
            FieldManagementScreen()
        }
        composable("marketplace") {
            MarketplaceScreen()
        }
        composable("orchard") {
            OrchardManagementScreen()
        }
        composable("planner") {
            PlannerScreen()
        }
        composable("vehicle") {
            VehicleManagementScreen(navController)
        }
        composable("warehouse") {
            WarehouseManagementScreen()
        }
        composable("announcement") {
            AnnouncementScreen()
        }
    }
}

@Composable
fun HomePage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Dodałem padding wokół ekranu, aby przyciski nie były przy krawędziach
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp) // Dodaje równomierne odstępy między przyciskami
    ) {
        Button(
            onClick = { navController.navigate("animals") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Zarządzanie zwierzętami", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("announcement") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Ogłoszenia", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("chat") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Chat", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("fields") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Zarządzanie polami", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("marketplace") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Rynek", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("planner") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Planer", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("orchard") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Zarządzanie sadami", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("vehicle") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Zarządzanie pojazdami", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            onClick = { navController.navigate("warehouse") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Zarządzanie magazynem", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

