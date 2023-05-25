package com.example.footballfantasy

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DB_NAME = "Login_db"
val TABLE_NAME_USERS = "Users"
val COL_LOGIN = "login"
val COL_ID = "id"

val TABLE_NAME_CLUBS = "FootballClubs"
val COL_CLUB_NAME = "clubName"
val COL_COUNTRY = "country"
val COL_MANAGER_NAME = "managerName"
val COL_COLOR = "color"

data class FootballClub(val clubName: String, val country: String, val managerName: String, val color: String)

class DataBaseHandler(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $TABLE_NAME_USERS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_LOGIN TEXT)"
        db?.execSQL(createUserTable)

        val createClubTable = "CREATE TABLE $TABLE_NAME_CLUBS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_LOGIN TEXT, " +
                "$COL_CLUB_NAME TEXT, " +
                "$COL_COUNTRY TEXT, " +
                "$COL_MANAGER_NAME TEXT, " +
                "$COL_COLOR TEXT)"
        db?.execSQL(createClubTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older tables if they exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_CLUBS")
        // Create tables again
        onCreate(db)
    }

    fun insertData(login: User): Long {
        if (isLoginExists(login.login)) {
            Toast.makeText(context, "Login already exists", Toast.LENGTH_SHORT).show()
            return -1L
        }

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LOGIN, login.login)
        val result = db.insert(TABLE_NAME_USERS, null, contentValues)
        if (result == -1L)
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        return result
    }

    fun insertClub(login: String, club: FootballClub): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LOGIN, login)
        contentValues.put(COL_CLUB_NAME, club.clubName)
        contentValues.put(COL_COUNTRY, club.country)
        contentValues.put(COL_MANAGER_NAME, club.managerName)
        contentValues.put(COL_COLOR, club.color)
        val result = db.insert(TABLE_NAME_CLUBS, null, contentValues)
        if (result == -1L)
            Toast.makeText(context, "Failed to insert club", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Club inserted successfully", Toast.LENGTH_SHORT).show()
        return result
    }

    fun isLoginExists(login: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT $COL_LOGIN FROM $TABLE_NAME_USERS WHERE $COL_LOGIN = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(login))
        val exists = cursor?.count ?: 0 > 0
        cursor?.close()
        return exists
    }
}
