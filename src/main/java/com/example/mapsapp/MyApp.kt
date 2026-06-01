package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.supabase.MySupabaseClient


class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        // Corregimos el orden: URL a supabaseUrl y KEY a supabaseKey
        database = MySupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        )
    }
}
