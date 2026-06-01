package com.example.mapsapp.data.supabase

import com.example.mapsapp.data.supabase.models.MarkerEntity
import com.example.mapsapp.data.utils.AuthResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.util.UUID

class MySupabaseClient() {
    lateinit var client: SupabaseClient

    constructor(supabaseUrl: String, supabaseKey: String) : this() {
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            install(Storage)
            install(Auth) {
                autoLoadFromStorage = true
            }
        }
    }
    

    // MARKERS
    suspend fun insertMarker(marker: MarkerEntity) {
        client.from("markers").insert(marker)
    }

    suspend fun getAllMarkers(): List<MarkerEntity> {
        return client.from("markers").select {}.decodeList<MarkerEntity>()
    }

    suspend fun deleteMarker(id: String) {
        client.from("markers").delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun updateMarker(id: String, marker: MarkerEntity) {
        client.from("markers").update(marker) {
            filter {
                eq("id", id)
            }
        }
    }


    // Auth

    suspend fun register(email: String, password: String): AuthResult {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al registrarse")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    suspend fun logout(): AuthResult {
        return try {
            client.auth.signOut()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al cerrar sesión")
        }
    }

    fun isLoggedIn(): Boolean {
        return client.auth.currentSessionOrNull() != null
    }

    fun currentSession(): UserSession? {
        return client.auth.currentSessionOrNull()
    }

    suspend fun uploadImage(imageFile: ByteArray): String {
        val fileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
        val bucket = client.storage.from("images")
        bucket.upload(path = fileName, data = imageFile) {
            upsert = true
        }
        return bucket.publicUrl(fileName)
    }

    suspend fun deleteImage(imageUrl: String) {
        try {
            val fileName = imageUrl.substringAfterLast("/")
            val bucket = client.storage.from("images")
            bucket.delete(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
