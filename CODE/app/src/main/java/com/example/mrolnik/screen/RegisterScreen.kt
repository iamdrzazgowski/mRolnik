package com.example.mrolnik.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mrolnik.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable

fun RegisterScreen(navController: NavController) {
    val email = remember { mutableStateOf("") }
    val imie = remember { mutableStateOf("") }
    val nazwisko = remember { mutableStateOf("") }
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val userService = UserService()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = imie.value,
                onValueChange = { imie.value = it },
                label = { Text("Imie") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = nazwisko.value,
                onValueChange = { nazwisko.value = it },
                label = { Text("Nazwisko") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = login.value,
                onValueChange = { login.value = it },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Hasło") },
                visualTransformation = PasswordVisualTransformation(), // Ukrycie hasła
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Powtórz hasło") },
                visualTransformation = PasswordVisualTransformation(), // Ukrycie hasła
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Button(onClick = {
                keyboardController?.hide()

                if (email.value.isBlank() || login.value.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank() || imie.value.isBlank() || nazwisko.value.isBlank()) {
                    imie.value = ""
                    nazwisko.value = ""
                    login.value = ""
                    password.value = ""
                    confirmPassword.value = ""
                    email.value = ""
                    focusManager.clearFocus()
                    CoroutineScope(Dispatchers.Main).launch {
                        snackbarHostState.showSnackbar(
                            message = "Wszystkie pola muszą być uzupełnione",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    return@Button
                }

                val digitsOrSpecials = Regex(".*[\\d\\W].*")
                if (digitsOrSpecials.containsMatchIn(imie.value) || digitsOrSpecials.containsMatchIn(nazwisko.value)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        imie.value = ""
                        nazwisko.value = ""
                        login.value = ""
                        password.value = ""
                        confirmPassword.value = ""
                        email.value = ""
                        focusManager.clearFocus()
                        snackbarHostState.showSnackbar(
                            message = "Imię lub nazwisko nie mogą zawierać cyfr lub znaków spejcalnuch",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    return@Button
                }


                val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
                if (!emailRegex.matches(email.value)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        imie.value = ""
                        nazwisko.value = ""
                        login.value = ""
                        password.value = ""
                        confirmPassword.value = ""
                        email.value = ""
                        focusManager.clearFocus()
                        snackbarHostState.showSnackbar(
                            message = "Niepoprawny format e-maila",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    return@Button
                }

                if (login.value.equals(password.value)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        login.value = ""
                        password.value = ""
                        confirmPassword.value = ""
                        snackbarHostState.showSnackbar(
                            message = "Login i hasło nie mogą być takie same",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    return@Button
                }


                if (password.value != confirmPassword.value) {
                    password.value = ""
                    confirmPassword.value = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        snackbarHostState.showSnackbar(
                            message = "Hasła nie są identyczne",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    return@Button
                }
                CoroutineScope(Dispatchers.IO).launch {
                    if (userService.registerUser(imie, nazwisko, login, password, email)) {
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(
                                message = "Rejestracja zakończona sukcesem",
                                duration = androidx.compose.material3.SnackbarDuration.Short
                            )
                            navController.navigate("login")
                        }
                    } else {
                        snackbarHostState.showSnackbar(
                            message = "Błąd podczas rejestracji",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                }
            }) {
                Text("Zarejestruj się")
            }
        }
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}