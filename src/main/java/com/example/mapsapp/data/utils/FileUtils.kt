package com.example.mapsapp.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File

object FileUtils {
    fun createImageUri(context: Context): Uri? {
        val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun Uri.toCompressedByteArray(context: Context): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(this)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            // Comprimimos al 80% para ahorrar espacio y tiempo de subida
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
