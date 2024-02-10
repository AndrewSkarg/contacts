package com.example.supercontacts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "contacts.db"
    }

    object ContactEntry : BaseColumns {
        const val TABLE_NAME = "contacts"
        const val _ID = BaseColumns._ID
        const val NAME = "name"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val PHOTO_URL = "photo_url"  // Замість PHOTO додано PHOTO_URL
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE ${ContactEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${ContactEntry.NAME} TEXT," +
                "${ContactEntry.PHONE} TEXT," +
                "${ContactEntry.EMAIL} TEXT," +
                "${ContactEntry.PHOTO_URL} TEXT)"  // Додано нове поле PHOTO_URL

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Ви можете додати логіку для оновлення бази даних при зміні версії
    }

    fun getAllContacts(): Cursor {
        val db = this.readableDatabase
        return db.query(ContactEntry.TABLE_NAME, null, null, null, null, null, null)
    }

    fun deleteContact(contactId: Long): Int {
        val db = writableDatabase
        return db.delete(ContactEntry.TABLE_NAME, "${ContactEntry._ID}=?", arrayOf(contactId.toString()))
    }

    fun updateContact(contactId: Long, name: String, phone: String, email: String, photoUrl: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.ContactEntry.NAME, name)
            put(DBHelper.ContactEntry.PHONE, phone)
            put(DBHelper.ContactEntry.EMAIL, email)
        }

        val rowsUpdated = db.update(DBHelper.ContactEntry.TABLE_NAME, values, "${DBHelper.ContactEntry._ID}=?", arrayOf(contactId.toString()))

        // Перевірте, чи були оновлені рядки і поверніть відповідне значення
        return rowsUpdated > 0
    }

}
