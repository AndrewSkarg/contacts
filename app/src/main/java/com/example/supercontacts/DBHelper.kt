package com.example.supercontacts

import android.content.Context
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
        const val NAME = "name"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val PHOTO = "photo"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE ${ContactEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${ContactEntry.NAME} TEXT," +
                "${ContactEntry.PHONE} TEXT)"

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Ви можете додати логіку для оновлення бази даних при зміні версії
    }
}
