package com.example.mrolnik.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FieldManagementScreen() {
    var fields by remember { mutableStateOf(listOf<String>()) }
    var selectedField by remember { mutableStateOf<String?>(null) }
    var fieldName by remember { mutableStateOf("") }
    var fieldCultivations by remember { mutableStateOf(mapOf<String, List<String>>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (selectedField == null) {
            OutlinedTextField(
                value = fieldName,
                onValueChange = { fieldName = it },
                label = { Text("Field Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (fieldName.isNotBlank()) {
                        fields = fields + fieldName
                        fieldCultivations = fieldCultivations + (fieldName to listOf())
                        fieldName = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Add Field")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("List of Fields:", style = MaterialTheme.typography.headlineSmall)
            Column {
                fields.forEach { field ->
                    Text(
                        text = field,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedField = field },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            FieldDetailScreen(
                fieldName = selectedField!!,
                cultivations = fieldCultivations[selectedField] ?: listOf(),
                onAddCultivation = { cultivation ->
                    fieldCultivations = fieldCultivations.toMutableMap().apply {
                        this[selectedField!!] = (this[selectedField] ?: listOf()) + cultivation
                    }
                },
                onBack = { selectedField = null }
            )
        }
    }
}

@Composable
fun FieldDetailScreen(fieldName: String, cultivations: List<String>, onAddCultivation: (String) -> Unit, onBack: () -> Unit) {
    var showCultivationForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Field: $fieldName", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Cultivations in this field:", style = MaterialTheme.typography.bodyLarge)
        Column {
            cultivations.forEach { cultivation ->
                Text(text = cultivation, modifier = Modifier.padding(4.dp))
            }
        }

        Button(
            onClick = { showCultivationForm = !showCultivationForm },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (showCultivationForm) "Hide Cultivation Form" else "Add Cultivation")
        }

        if (showCultivationForm) {
            CultivationForm(onAddCultivation)
        }

        Button(
            onClick = onBack,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Back to Fields")
        }
    }
}

@Composable
fun CultivationForm(onAddCultivation: (String) -> Unit) {
    var plantName by remember { mutableStateOf("") }
    var sowingDate by remember { mutableStateOf("") }
    var harvestDate by remember { mutableStateOf("") }
    var fertilizerQty by remember { mutableStateOf("") }
    var sprayingQty by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = plantName,
            onValueChange = { plantName = it },
            label = { Text("Plant Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = sowingDate,
            onValueChange = { sowingDate = it },
            label = { Text("Sowing Date") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = harvestDate,
            onValueChange = { harvestDate = it },
            label = { Text("Planned Harvest Date") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fertilizerQty,
            onValueChange = { fertilizerQty = it },
            label = { Text("Used Fertilizer Quantity") },
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
                if (plantName.isNotBlank() && sowingDate.isNotBlank() && harvestDate.isNotBlank() && fertilizerQty.isNotBlank() && sprayingQty.isNotBlank()) {
                    onAddCultivation("$plantName - $sowingDate - $harvestDate - Fertilizer: $fertilizerQty kg - Spraying: $sprayingQty L")
                    plantName = ""
                    sowingDate = ""
                    harvestDate = ""
                    fertilizerQty = ""
                    sprayingQty = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Cultivation Data")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFieldManagement() {
    FieldManagementScreen()
}
