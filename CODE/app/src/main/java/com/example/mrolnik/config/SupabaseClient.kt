package com.example.mrolnik.config

import io.github.cdimascio.dotenv.dotenv
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.JacksonSerializer

class SupabaseClient {
    val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    val supabaseUrl = dotenv["SUPABASE_URL"] ?: throw IllegalStateException("SUPABASE_URL is missing")
    val supabaseKey = dotenv["SUPABASE_KEY"] ?: throw IllegalStateException("SUPABASE_KEY is missing")

    private var supabase: SupabaseClient? = null

    fun getSupabaseClient(): SupabaseClient {
        if (supabase == null) {
            supabase = createSupabaseClient(supabaseUrl, supabaseKey) {
                install(Postgrest)
                defaultSerializer = JacksonSerializer()
            }
        }
        return supabase as SupabaseClient
    }
}


