package com.example.footballfantasy

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

val DB_NAME = "Login_db"
val TABLE_NAME_USERS = "Users"
val COL_LOGIN = "login"
val COL_ID = "id"


data class FootballClub(val clubName: String, val country: String, val managerName: String, val clubRating: String)

class DataBaseHandler(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 2) {

    val TABLE_NAME_PREFIX = "ClubTable_"
    val COL_CLUB_NAME = "clubName"
    val COL_COUNTRY = "country"
    val COL_MANAGER_NAME = "managerName"
    val COL_CLUB_RATING = "clubRating"



    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $TABLE_NAME_USERS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_LOGIN TEXT)"
        db?.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older tables if they exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_USERS")
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
        Log.d("DataBaseHandler", "Inserting club: ${club.clubName}")
        val db = this.writableDatabase
        val clubTableName = TABLE_NAME_PREFIX + login // Generate a unique table name for each login
        val createClubTable = "CREATE TABLE IF NOT EXISTS $clubTableName (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_CLUB_NAME TEXT, " +
                "$COL_COUNTRY TEXT, " +
                "$COL_MANAGER_NAME TEXT, " +
                "$COL_CLUB_RATING TEXT)"
        db.execSQL(createClubTable)

        val contentValues = ContentValues()
        contentValues.put(COL_CLUB_NAME, club.clubName)
        contentValues.put(COL_COUNTRY, club.country)
        contentValues.put(COL_MANAGER_NAME, club.managerName)
        contentValues.put(COL_CLUB_RATING, club.clubRating)

        val result = db.insert(clubTableName, null, contentValues)
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

    fun getClubTableData(login: String): Cursor? {
        val db = this.readableDatabase
        val clubTableName = TABLE_NAME_PREFIX + login // Generate the corresponding table name for the login
        val query = "SELECT * FROM $clubTableName" // Retrieve all rows from the login-specific table

        // Add log statement to display the query
        Log.d("DataBaseHandler", "Query: $query")

        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            // Add log statements to display the result count and move to the first row
            Log.d("DataBaseHandler", "Result count: ${cursor.count}")
            cursor.moveToFirst()
        } else {
            // Log statement to indicate that the cursor is null
            Log.d("DataBaseHandler", "Cursor is null")
        }

        return cursor
    }

    fun getClubsByLogin(login: String): List<FootballClub> {
        val clubList = mutableListOf<FootballClub>()
        val db = readableDatabase
        val clubTableName = TABLE_NAME_PREFIX + login
        try {
            val cursor = db.rawQuery("SELECT * FROM $clubTableName", null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val clubName = cursor.getString(cursor.getColumnIndex(COL_CLUB_NAME))
                    val country = cursor.getString(cursor.getColumnIndex(COL_COUNTRY))
                    val managerName = cursor.getString(cursor.getColumnIndex(COL_MANAGER_NAME))
                    val clubRating = cursor.getString(cursor.getColumnIndex(COL_CLUB_RATING))
                    val club = FootballClub(clubName, country, managerName, clubRating)
                    clubList.add(club)
                    cursor.moveToNext()
                }
            }
            cursor.close()
        } catch (e: SQLiteException) {
            // Handle the exception here
            return emptyList() // or return a default value as needed
        } finally {
            db.close()
        }
        return clubList
    }

}
