package com.example.supercontacts

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AddContactActivity: AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var photoImageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var photoUrl: String

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        photoUrl="";
        dbHelper = DBHelper(this)

        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        photoImageView = findViewById(R.id.imageViewPhoto)
        saveButton = findViewById(R.id.buttonSave)

        photoImageView.setOnClickListener {
            dispatchTakePictureIntent()
        }

        saveButton.setOnClickListener {
            saveContact()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoImageView.setImageBitmap(imageBitmap)

            val photoUrl = saveImageToCache(imageBitmap)

            setPhotoUrl(photoUrl)
        }
    }

    private fun saveImageToCache(bitmap: Bitmap): String {
        val uniqueId = UUID.randomUUID().toString()

        val fileName = "contact_image_$uniqueId.jpg"
        val file = File(cacheDir, fileName)

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

    private fun saveContact() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val email = emailEditText.text.toString()

        val newRowId = dbHelper.saveContact(name, phone, email, photoUrl)

        if (newRowId > -1) {
            Toast.makeText(this, "Контакт збережено: $name, $phone, $email", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Помилка збереження контакту", Toast.LENGTH_SHORT).show()
            setResult(RESULT_CANCELED)
        }
    }



    private fun setPhotoUrl(url: String) {
        photoUrl = url
    }
    private fun cancelContact() {
        setResult(RESULT_CANCELED)
        finish()
    }
    override fun onBackPressed() {
        cancelContact()
    }

}