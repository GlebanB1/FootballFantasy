package com.example.footballfantasy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DB_NAME = "Login_db"
val TABLE_NAME = "Users"
val COL_LOGIN = "login"
val COL_ID = "id"

class DataBaseHandler(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_LOGIN VARCHAR(256))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        // Create tables again
        onCreate(db)
    }

    fun insertData(login: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LOGIN, login.login) // Pass login.login instead of login
        val result = db.insert(TABLE_NAME, null, contentValues)
        if (result == -1L)
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        return result
    }

}
