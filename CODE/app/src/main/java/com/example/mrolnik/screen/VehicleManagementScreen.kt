package com.example.mrolnik.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mrolnik.model.Animal
import com.example.mrolnik.model.Vehicle
import com.example.mrolnik.service.UserService
import com.example.mrolnik.service.VehicleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun VehicleManagementScreen(navController: NavController) {
    var showForm by remember { mutableStateOf(false) }
    var newVehicle by remember { mutableStateOf("") }
    var newVehicleCondition by remember { mutableStateOf("") }
    var vehicle: Vehicle
    var vehicleService = VehicleService()
    var vehicles by remember { mutableStateOf(emptyList<Vehicle>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { showForm = true }) {
            Text("Add Vehicle")
        }

        if (showForm) {
            OutlinedTextField(
                value = newVehicle,
                onValueChange = { newVehicle = it },
                label = { Text("Vehicle Name") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = newVehicleCondition,
                onValueChange = { newVehicleCondition = it },
                label = { Text("Vehicle Condition") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (newVehicle.isNotBlank() && newVehicleCondition.isNotBlank()) {
                            vehicle = Vehicle(newVehicle, newVehicleCondition)
                            vehicleService.addVehicle(vehicle)
                            vehicleService.addVehicleIdToAssociationTable()
                            newVehicle = ""
                            newVehicleCondition = ""
                            showForm = false
                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Save Vehicle")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Vehicle List:", style = MaterialTheme.typography.headlineSmall)
        LaunchedEffect(Unit) {
            val fetchedVehicles = withContext(Dispatchers.IO) {
                vehicleService.getAllByUserId()
            }
            vehicles = fetchedVehicles
        }
        LazyColumn {
            if (vehicles.isNotEmpty()) {
                items(vehicles) { vehicle ->
                    Text(
                        text = "${vehicle.vehicleName} - ${vehicle.technicalCondition}",
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                items(vehicles) { vehicle ->
                    Text(
                        text = "Brak pojazdów",
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}