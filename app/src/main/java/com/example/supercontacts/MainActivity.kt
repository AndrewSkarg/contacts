package com.example.supercontacts

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var listView: ListView
    private lateinit var adapter: SimpleCursorAdapter
    companion object {
        private const val ADD_CONTACT_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        listView = findViewById(R.id.listViewContacts)

        // Отримання даних з бази даних і створення адаптера
        val cursor: Cursor = dbHelper.getAllContacts()
        val fromColumns = arrayOf(
            DBHelper.ContactEntry.PHOTO_URL,
            DBHelper.ContactEntry.NAME,
            DBHelper.ContactEntry.PHONE
        )
        val toViews = intArrayOf(
            R.id.imageViewContact,
            R.id.textViewName,
            R.id.textViewPhone
        )

        adapter = SimpleCursorAdapter(
            this,
            R.layout.list_item_contact,
            cursor,
            fromColumns,
            toViews,
            0
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            // Отримайте курсор для поточного елемента
            val cursor = parent.getItemAtPosition(position) as Cursor

            // Отримайте дані з курсора
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry._ID))
            Log.d("lite","contact id "+contactId)
            val contactName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.NAME))
            val contactPhone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.PHONE))
            val contactPhotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.PHOTO_URL))
            val contactEmail = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.EMAIL))

            // Викличте нову активність або здійсніть інші дії з отриманими даними
            // Наприклад, ви можете відкрити нову активність для перегляду деталей контакту
            openContactDetails(contactId,contactEmail,contactName, contactPhone, contactPhotoUrl)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_contact -> {
                addContact()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addContact() {
        val intent = Intent(this, AddContactActivity::class.java)
        startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE  && resultCode == RESULT_OK) {
            // Оновіть дані адаптера тут
            adapter.changeCursor(dbHelper.getAllContacts())
        }
    }
    private fun openContactDetails(contactId:Long,contactEmail:String, contactName: String, contactPhone: String, contactPhotoUrl: String) {
        // Створіть інтент для відкриття деталей контакту
        val intent = Intent(this, ContactDetailsActivity::class.java)

        // Передайте дані контакту через інтент
        intent.putExtra("contact_id",contactId )

        intent.putExtra("contact_name", contactName)
        intent.putExtra("contact_phone", contactPhone)
        intent.putExtra("contact_photo_url", contactPhotoUrl)
        intent.putExtra("contact_email", contactEmail)  // Додано передачу електронної пошти

        // Запустіть нову активність
        startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)
    }
}

