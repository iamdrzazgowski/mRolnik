package com.example.mrolnik.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun OrchardManagementScreen() {
    var orchards by remember { mutableStateOf(listOf<String>()) }
    var selectedOrchard by remember { mutableStateOf<String?>(null) }
    var orchardName by remember { mutableStateOf("") }
    var orchardFruitTrees by remember { mutableStateOf(mapOf<String, List<String>>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (selectedOrchard == null) {
            OutlinedTextField(
                value = orchardName,
                onValueChange = { orchardName = it },
                label = { Text("Orchard Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (orchardName.isNotBlank()) {
                        orchards = orchards + orchardName
                        orchardFruitTrees = orchardFruitTrees + (orchardName to listOf())
                        orchardName = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Add Orchard")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("List of Orchards:", style = MaterialTheme.typography.headlineSmall)
            Column {
                orchards.forEach { orchard ->
                    Text(
                        text = orchard,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedOrchard = orchard },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            OrchardDetailScreen(
                orchardName = selectedOrchard!!,
                resources = orchardFruitTrees[selectedOrchard] ?: listOf(),
                onAddFruitTree = { resource ->
                    orchardFruitTrees = orchardFruitTrees.toMutableMap().apply {
                        this[selectedOrchard!!] = (this[selectedOrchard] ?: listOf()) + resource
                    }
                },
                onBack = { selectedOrchard = null }
            )
        }
    }
}

@Composable
fun OrchardDetailScreen(orchardName: String, resources: List<String>, onAddFruitTree: (String) -> Unit, onBack: () -> Unit) {
    var showFruitTreeForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Orchard: $orchardName", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Fruit Trees in this orchard:", style = MaterialTheme.typography.bodyLarge)
        Column {
            resources.forEach { resource ->
                Text(text = resource, modifier = Modifier.padding(4.dp))
            }
        }

        Button(
            onClick = { showFruitTreeForm = !showFruitTreeForm },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (showFruitTreeForm) "Hide Tree Form" else "Add Fruit Tree")
        }

        if (showFruitTreeForm) {
            FruitTreeForm(onAddFruitTree)
        }

        Button(
            onClick = onBack,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Back to Orchards")
        }
    }
}

@Composable
fun FruitTreeForm(onAddFruitTree: (String) -> Unit) {
    var plantName by remember { mutableStateOf("") }
    var harvestDate by remember { mutableStateOf("") }
    var sprayingQty by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = plantName,
            onValueChange = { plantName = it },
            label = { Text("Plant Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = harvestDate,
            onValueChange = { harvestDate = it },
            label = { Text("Planned Harvest Date") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = sprayingQty,
            onValueChange = { sprayingQty = it },
            label = { Text("Used Spraying Quantity") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (plantName.isNotBlank() && harvestDate.isNotBlank() && sprayingQty.isNotBlank()) {
                    onAddFruitTree("$plantName - $harvestDate - $sprayingQty L")
                    plantName = ""
                    harvestDate = ""
                    sprayingQty = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Tree Data")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrchardManagement() {
    OrchardManagementScreen()
}
