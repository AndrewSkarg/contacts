package com.example.supercontacts

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var photoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)  // Змініть на свою розмітку

        // Отримайте дані контакту з інтенту
        val contactId = intent.getLongExtra("contact_id", -1)
        val contactName = intent.getStringExtra("contact_name") ?: ""
        val contactPhone = intent.getStringExtra("contact_phone") ?: ""
        val contactPhotoUrl = intent.getStringExtra("contact_photo_url") ?: ""
        val contactEmail = intent.getStringExtra("contact_email") ?: ""

        // Ініціалізуйте ваші елементи з розмітки
        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        photoImageView = findViewById(R.id.imageViewPhoto)

        // Заповніть ваші елементи даними контакту
        nameEditText.setText(contactName)
        phoneEditText.setText(contactPhone)
        emailEditText.setText(contactEmail)
        val bitmap = BitmapFactory.decodeFile(contactPhotoUrl)
        photoImageView.setImageBitmap(bitmap)

        // Ви можете додати обробку для відображення електронної пошти і фото також


        // Завантажте та відобразіть фото, якщо воно доступне
        // Ви можете використовувати бібліотеки для завантаження та відображення зображень, наприклад, Picasso або Glide
        // Додайте залежності для бібліотек у файл build.gradle (Module: app)
        // implementation 'com.squareup.picasso:picasso:2.71828' (для Picasso)
        // implementation 'com.github.bumptech.glide:glide:4.12.0' (для Glide)
        // Використовуйте їх для завантаження та відображення фото
        // Picasso.get().load(contactPhotoUrl).into(photoImageView) (для Picasso)
        // Glide.with(this).load(contactPhotoUrl).into(photoImageView) (для Glide)
    }
}
