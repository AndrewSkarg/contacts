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
    private lateinit var photoImageView: ImageView  // Додано поле для зображення
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
        photoImageView = findViewById(R.id.imageViewPhoto)  // Ідентифікатор для ImageView
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

            // Зберегти зображення в кеші додатку і отримати його посилання
            val photoUrl = saveImageToCache(imageBitmap)

            // Встановити посилання на зображення
            setPhotoUrl(photoUrl)
        }
    }

    private fun saveImageToCache(bitmap: Bitmap): String {
// Генеруйте унікальний ідентифікатор
        val uniqueId = UUID.randomUUID().toString()

        // Створіть файл у кеші додатку з унікальним ідентифікатором
        val fileName = "contact_image_$uniqueId.jpg"
        val file = File(cacheDir, fileName)

        // Використовуйте FileOutputStream для запису зображення у файл
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            // Обробіть виняток, якщо не вдалося зберегти зображення
        }

        // Поверніть посилання на зображення (URI або шлях до файлу)
        return file.absolutePath
    }

    private fun saveContact() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val email = emailEditText.text.toString()

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DBHelper.ContactEntry.NAME, name)
            put(DBHelper.ContactEntry.PHONE, phone)
            put(DBHelper.ContactEntry.EMAIL, email)
            // Перевірте, чи photoUrl не порожній перед додаванням його в ContentValues
            if (photoUrl.isNotEmpty()) {
                put(DBHelper.ContactEntry.PHOTO_URL, photoUrl)
            }
        }

        val newRowId = db.insert(DBHelper.ContactEntry.TABLE_NAME, null, values)

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
        // Викликайте cancelContact(), коли користувач натискає кнопку "Назад"
        cancelContact()
    }

}