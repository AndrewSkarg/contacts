package com.example.supercontacts

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PhotoUtils(private val activity: AppCompatActivity) {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun saveImageToCache(bitmap: Bitmap): String {
        val uniqueId = UUID.randomUUID().toString()

        val fileName = "contact_image_$uniqueId.jpg"
        val file = File(activity.cacheDir, fileName)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }
}
