package com.example.mrolnik.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ExpandLess
//import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mrolnik.R
import com.example.mrolnik.model.FruitTree
//import com.example.mrolnik.model.Resource
import com.example.mrolnik.service.FruitTreeService
import com.example.mrolnik.viewmodel.LocalSharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class fruitTreeInputField(val label: String, val value: String)

val fruitTreeService = FruitTreeService()

@Composable
fun FruitTreesScreen(navController: NavController) {
    val sharedOrchardViewModel = LocalSharedViewModel.current
    val orchardState = sharedOrchardViewModel.selectedOrchard.collectAsState()
    val currentOrchard = orchardState.value // Aktualny sad możesz wziąć z niego ID

    val backIcon = painterResource(R.drawable.baseline_arrow_back)
    val addIcon = painterResource(id = R.drawable.baseline_add)

    var expandedIndex by remember { mutableStateOf<Int?>(null) }


    var showAddFruitTreeDialog by remember { mutableStateOf(false) }

    var fruitTrees by remember { mutableStateOf(emptyList<FruitTree>()) }

    // LISTA inputFieldów dla dodawania zasobu
    val fruitTreesInputField = listOf(
        fruitTreeInputField("Nazwa", ""),
        fruitTreeInputField("Planowany zbiór", ""),
        fruitTreeInputField("Jakość oprysków", ""),
    )

    // Lista z wartościami
    var inputFruitTreesFieldValues by remember { mutableStateOf(fruitTreesInputField.associateWith { it.value }) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = backIcon,
                    contentDescription = "Wróć",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Drzewka owocowe w sadzie ${currentOrchard?.orchardName}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )

        }

        Button(
            onClick = { showAddFruitTreeDialog = true },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                painter = addIcon,
                contentDescription = "ADD",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(Unit) {
            fruitTrees = fruitTreeService.getAllFruitTreesByOrchardId(currentOrchard)
        }

        LazyColumn {
            items(fruitTrees) { fruitTree ->
                FruitTreeItem(
                    fruitTree = fruitTree,
                    isExpanded = expandedIndex == fruitTrees.indexOf(fruitTree),
                    onClick = {
                        expandedIndex = if (expandedIndex == fruitTrees.indexOf(fruitTree)) null else fruitTrees.indexOf(fruitTree)
                    },
                    navController
                )
            }
        }

        if(showAddFruitTreeDialog){
            CustomModalDialog(
                onDismiss = { showAddFruitTreeDialog = false },
                title = "Dodaj naprawę",
                onConfirm = {
                    val fieldValues = inputFruitTreesFieldValues.mapKeys { it.key.label }

                    val plantName = fieldValues["Nazwa"] ?: ""
                    val plannedHarvestDate = fieldValues["Planowany zbiór"] ?: ""
                    val usedSprayingQuantity = fieldValues["Jakość oprysków"]?.toDoubleOrNull() ?: 0.0

                    val fruitTree = FruitTree(plantName, plannedHarvestDate, usedSprayingQuantity)

                    CoroutineScope(Dispatchers.IO).launch {
                        fruitTreeService.assignFruitTreeToOrchard(fruitTree, currentOrchard)
                        fruitTrees = fruitTreeService.getAllFruitTreesByOrchardId(currentOrchard)
                    }
                    showAddFruitTreeDialog = false
                },
                content = {
                    fruitTreesInputField.forEach { inputField ->
                        TextField(
                            value = inputFruitTreesFieldValues[inputField] ?: "",
                            onValueChange = { newValue ->
                                inputFruitTreesFieldValues = inputFruitTreesFieldValues.toMutableMap().apply {
                                    this[inputField] = newValue
                                }
                            },
                            label = { Text(inputField.label) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Wpisz ${inputField.label}") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            )
        }
    }
}

@Composable
fun FruitTreeItem(
    fruitTree: FruitTree,
    isExpanded: Boolean,
    onClick: () -> Unit,
    navController: NavController
) {
    var showFruitTreeDialog by remember { mutableStateOf(false) }
    val sharedViewModel = LocalSharedViewModel.current

    // LISTA inputFieldów dla dodawania zasobu
    val fruitTreesInputField = listOf(
        fruitTreeInputField("Nazwa", fruitTree.plantName),
        fruitTreeInputField("Planowany zbiór", fruitTree.plannedHarvestDate),
        fruitTreeInputField("Jakość oprysków", fruitTree.usedSprayingQuantity.toString()),
    )

    // Lista z wartościami
    var inputFruitTreesFieldValues by remember { mutableStateOf(fruitTreesInputField.associateWith { it.value }) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fruitTree.plantName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
//                Icon(
//                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
//                    contentDescription = null
//                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Nazwa: ${fruitTree.plantName}")
                Text("Data planowanych zbiorów: ${fruitTree.plannedHarvestDate}")
                Text("Jakość użytego spryskiwacza: ${fruitTree.usedSprayingQuantity}")

                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { sharedViewModel.selectFruitTree(fruitTree)
                              navController.navigate("sprayingHistory")},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sprawdź historię oprysków")
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { showFruitTreeDialog = true }) {
                        Text("Edytuj")
                    }

                    Button(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            fruitTreeService.deleteFruitTree(fruitTree)
                        }
                    }) {
                        Text("Usuń")
                    }
                }
            }
            if (showFruitTreeDialog) {
                CustomModalDialog(
                    onDismiss = { showFruitTreeDialog = false },
                    title = "Edytuj: ${fruitTree.plantName}",
                    onConfirm = {
                        val fieldValues = inputFruitTreesFieldValues.mapKeys { it.key.label }
                        val plantName = fieldValues["Nazwa"] ?: ""
                        val plannedHarvestDate = fieldValues["Planowany zbiór"] ?: ""
                        val usedSprayingQuantity = fieldValues["Jakość oprysków"]?.toDoubleOrNull() ?: 0.0
                        fruitTree.plantName = plantName
                        fruitTree.plannedHarvestDate = plannedHarvestDate
                        fruitTree.usedSprayingQuantity = usedSprayingQuantity

                        CoroutineScope(Dispatchers.IO).launch {
                            fruitTreeService.updateFruitTree(fruitTree)
                        }

                        showFruitTreeDialog = false },
                    content = {
                        fruitTreesInputField.forEach { inputField ->
                            TextField(
                                value = inputFruitTreesFieldValues[inputField] ?: "",
                                onValueChange = { newValue ->
                                    inputFruitTreesFieldValues = inputFruitTreesFieldValues.toMutableMap().apply {
                                        this[inputField] = newValue
                                    } },
                                label = { Text(inputField.label) },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Wpisz ${inputField.label}") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                )
            }
        }
    }
}