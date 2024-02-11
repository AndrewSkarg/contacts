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
import android.widget.Button
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var listView: ListView
    private lateinit var addButton: Button

    private lateinit var adapter: SimpleCursorAdapter
    companion object {
        private const val ADD_CONTACT_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dbHelper = DBHelper(this)
        listView = findViewById(R.id.listViewContacts)
        addButton = findViewById(R.id.addBtn);
        addButton.setOnClickListener {
            addContact()
        }
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
            val cursor = parent.getItemAtPosition(position) as Cursor

            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry._ID))
            Log.d("lite","contact id "+contactId)
            val contactName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.NAME))
            val contactPhone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.PHONE))
            var contactPhotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.PHOTO_URL))
            val contactEmail = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ContactEntry.EMAIL))
            if (contactPhotoUrl===null){
                contactPhotoUrl="";
            }
            openContactDetails(contactId,contactEmail,contactName, contactPhone, contactPhotoUrl)
        }
    }





    private fun addContact() {
        val intent = Intent(this, AddContactActivity::class.java)
        startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE  && resultCode == RESULT_OK) {
            adapter.changeCursor(dbHelper.getAllContacts())
        }
    }
    private fun openContactDetails(contactId:Long,contactEmail:String, contactName: String, contactPhone: String, contactPhotoUrl: String) {
        val intent = Intent(this, ContactDetailsActivity::class.java)

        intent.putExtra("contact_id",contactId )

        intent.putExtra("contact_name", contactName)
        intent.putExtra("contact_phone", contactPhone)
        intent.putExtra("contact_photo_url", contactPhotoUrl)
        intent.putExtra("contact_email", contactEmail)

        startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)
    }
}

