package com.example.supercontacts

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddContactActivity: AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        dbHelper = DBHelper(this)

        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        saveButton = findViewById(R.id.buttonSave)

        saveButton.setOnClickListener {
            saveContact()
        }
    }

    private fun saveContact() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DBHelper.ContactEntry.NAME, name)
            put(DBHelper.ContactEntry.PHONE, phone)
        }

        val newRowId = db.insert(DBHelper.ContactEntry.TABLE_NAME, null, values)

        if (newRowId > -1) {
            Toast.makeText(this, "Контакт збережено: $name, $phone", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Помилка збереження контакту", Toast.LENGTH_SHORT).show()
        }
    }
}
