package com.example.supercontacts

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.widget.Toolbar
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
        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Оновіть дані адаптера тут
            adapter.changeCursor(dbHelper.getAllContacts())
        }
    }

}

