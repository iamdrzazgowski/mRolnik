package com.example.mrolnik.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

data class Animal(val name: String, val count: Int)


@Composable
fun AnimalsManagementScreen(navController: NavController) {
    var animals by remember { mutableStateOf(listOf(Animal("Dog", 2), Animal("Cat", 3), Animal("Horse", 1))) }
    var showForm by remember { mutableStateOf(false) }
    var newAnimal by remember { mutableStateOf("") }
    var newAnimalCount by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { showForm = true }) {
            Text("Add Animal")
        }

        if (showForm) {
            OutlinedTextField(
                value = newAnimal,
                onValueChange = { newAnimal = it },
                label = { Text("Animal Name") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = newAnimalCount,
                onValueChange = { newAnimalCount = it.filter { it.isDigit() } },
                label = { Text("Animal Count") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Button(
                onClick = {
                    if (newAnimal.isNotBlank() && newAnimalCount.isNotBlank()) {
                        animals = animals + Animal(newAnimal, newAnimalCount.toInt())
                        newAnimal = ""
                        newAnimalCount = ""
                        showForm = false
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Save Animal")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Animal List:", style = MaterialTheme.typography.headlineSmall)
        LazyColumn {
            items(animals) { animal ->
                Text(
                    text = "${animal.name} - ${animal.count} pcs",
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}