package com.example.footballfantasy
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.util.*

val DB_NAME = "Login_db"
val TABLE_NAME_USERS = "Users"
val COL_LOGIN = "login"
val COL_ID = "id"

// for each login their own club table

// can be multiple users on one phone

// for example for login "riyad" one table of clubs
// and if you enter another login you will see only clubs which was created under this login

// also it is saving date of creation and date of edition but i dont display it

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
                "$COL_CLUB_RATING TEXT, " +
                "creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
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

    fun searchClub(login: String, searchQuery: String): List<FootballClub> {
        val clubList = mutableListOf<FootballClub>()
        val db = readableDatabase
        val clubTableName = TABLE_NAME_PREFIX + login

        try {
            val query = "SELECT * FROM $clubTableName WHERE $COL_CLUB_NAME LIKE ?"
            val cursor = db.rawQuery(query, arrayOf("%$searchQuery%"))

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

        // Retrieve the login ID from the Users table
        val loginId = getLoginId(login)

        if (loginId == -1L) {
            // Handle the case where the login is not found
            return clubList
        }

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

    private fun getLoginId(login: String): Long {
        val db = readableDatabase
        val query = "SELECT $COL_ID FROM $TABLE_NAME_USERS WHERE $COL_LOGIN = ?"
        val cursor = db.rawQuery(query, arrayOf(login))
        val loginId = if (cursor.moveToFirst()) {
            cursor.getLong(cursor.getColumnIndex(COL_ID))
        } else {
            -1L // Return -1 if the login is not found
        }
        cursor.close()
        return loginId
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }



    fun updateClub(login: String, club: FootballClub, newClub: FootballClub) {
        val db = writableDatabase
        val clubTableName = TABLE_NAME_PREFIX + login
        val contentValues = ContentValues()
        contentValues.put(COL_CLUB_NAME, newClub.clubName)
        contentValues.put(COL_COUNTRY, newClub.country)
        contentValues.put(COL_MANAGER_NAME, newClub.managerName)
        contentValues.put(COL_CLUB_RATING, newClub.clubRating)
        contentValues.put("editionDate", getCurrentDateTime()) // Save the current date and time as the edition date
        db.update(
            clubTableName,
            contentValues,
            "$COL_CLUB_NAME = ? AND $COL_COUNTRY = ? AND $COL_MANAGER_NAME = ? AND $COL_CLUB_RATING = ?",
            arrayOf(club.clubName, club.country, club.managerName, club.clubRating)
        )
        db.close()
    }


    fun deleteClub(login: String, club: FootballClub) {
        val db = writableDatabase
        val clubTableName = TABLE_NAME_PREFIX + login
        val whereClause = "$COL_CLUB_NAME = ? AND $COL_COUNTRY = ? AND $COL_MANAGER_NAME = ? AND $COL_CLUB_RATING = ?"
        val whereArgs = arrayOf(club.clubName, club.country, club.managerName, club.clubRating)
        db.delete(clubTableName, whereClause, whereArgs)
        db.close()
    }

    fun deleteAllClubs(login: String) {
        val clubList = getClubsByLogin(login)

        if (clubList.isEmpty()) {
            Toast.makeText(context, "No clubs found for this login.", Toast.LENGTH_SHORT).show()
            return
        }

        val clubNames = clubList.map { it.clubName }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select a club to delete")

        builder.setItems(clubNames.toTypedArray()) { _, position ->
            val selectedClub = clubList[position]
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle("Delete Club")
            dialogBuilder.setMessage("Are you sure you want to delete ${selectedClub.clubName}?")

            dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                deleteClub(login, selectedClub)
                Toast.makeText(context, "Club deleted successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
