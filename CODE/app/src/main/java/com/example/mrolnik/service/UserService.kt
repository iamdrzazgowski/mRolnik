package com.example.mrolnik.service

import com.example.mrolnik.model.User
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.mrolnik.config.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserService {
    val supabase = SupabaseClient().getSupabaseClient()

    suspend fun loginUser(login: MutableState<String>, password: MutableState<String>): User? {
       try {
            val result: List<User> = supabase
                .from("user")
                .select {
                    filter {
                        eq("login", login.value.trim()) // trim() aby ucinalo biale znaki w loginie
                        eq("password", password.value)
                    }
                }
                .decodeList<User>()
            if (!result.isNullOrEmpty()) return result[0]
        } catch (e: Exception) {
            Log.e("UserService", "Login error")
            return null
        }
        return null
    }

    suspend fun registerUser(firstName: MutableState<String>, lastName: MutableState<String>,
                             login: MutableState<String>, password: MutableState<String>, email: MutableState<String>): Boolean {
        try {
            supabase
                .from("user")
                .insert(
                    mapOf(
                        "firstName" to firstName.value,
                        "lastName" to lastName.value,
                        "login" to login.value,
                        "password" to password.value,
                        "email" to email.value
                    )
                )
            return true
        } catch (e: Exception) {
            Log.e("UserService", "Register error")
            return false
        }
    }
}