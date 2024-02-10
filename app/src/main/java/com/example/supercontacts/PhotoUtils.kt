package com.example.supercontacts

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PhotoUtils(private val activity: Activity) {

    companion object {
        fun deletePhoto(photoUrl: String) {
            val file = File(photoUrl)

            if (file.exists()) {
                Log.d("lite","i delete photo "+photoUrl);
                file.delete()
            }
        }
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

        val fileName = "contact_image_$uniqueId.png"
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

    fun loadContactImage(imageView: ImageView, imageUrl: String) {
        Glide.with(activity)

            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground) // placeholder, якщо зображення не завантажено
            .into(imageView)
    }
}
