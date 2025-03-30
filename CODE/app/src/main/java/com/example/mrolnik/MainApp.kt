package com.example.mrolnik

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mrolnik.screen.HomeScreen
import com.example.mrolnik.screen.LoginScreen
import com.example.mrolnik.screen.MainScreen
import com.example.mrolnik.screen.RegisterScreen

@Composable
@Preview
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen() }
    }
}