package com.example.footballfantasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

// this page is for looking and searching clubs from table
class GameActivity : AppCompatActivity() {
    private lateinit var login: String
    private lateinit var dbHelper: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()
        login = intent.getStringExtra("login") ?: ""
        dbHelper = DataBaseHandler(this)

        val btnStart = findViewById<Button>(R.id.btnStart1)
        val btnBack = findViewById<Button>(R.id.btnback1)

        btnBack.setOnClickListener {
            if (login != null) {
                val clubs = dbHelper.getClubsByLogin(login!!)
                if (clubs.isNotEmpty()) {
                    val intent = Intent(this, StartGame::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No clubs found for the login", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No login found", Toast.LENGTH_SHORT).show()
            }
        }

        btnStart.setOnClickListener {
            if (login != null) {
                val clubs = dbHelper.getClubsByLogin(login!!)
                if (clubs.isNotEmpty()) {
                    val intent = Intent(this, MatchActivity::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No clubs found for the login", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No login found", Toast.LENGTH_SHORT).show()
            }
        }

        val login = intent.getStringExtra("login")
        Log.d("GameActivity", "Login: $login")
        if (login != null) {
            val dbHelper = DataBaseHandler(this)

            val cursor = dbHelper.getClubTableData(login)

            Log.d("GameActivity", "Cursor is null or empty: ${cursor == null || cursor.count == 0}")

            if (cursor != null && cursor.moveToFirst()) {
                val stringBuilder = StringBuilder()

                val colClubName = dbHelper.COL_CLUB_NAME
                val colCountry = dbHelper.COL_COUNTRY
                val colManagerName = dbHelper.COL_MANAGER_NAME
                val colClubRating = dbHelper.COL_CLUB_RATING

                do {
                    val clubName = cursor.getString(cursor.getColumnIndex(colClubName))
                    val country = cursor.getString(cursor.getColumnIndex(colCountry))
                    val managerName = cursor.getString(cursor.getColumnIndex(colManagerName))
                    val rating = cursor.getString(cursor.getColumnIndex(colClubRating))

                    stringBuilder.append("Club Name: $clubName\n")
                    stringBuilder.append("Country: $country\n")
                    stringBuilder.append("Manager Name: $managerName\n")
                    stringBuilder.append("Rating: $rating\n\n")

                } while (cursor.moveToNext())

                Log.d("GameActivity", "Club table data: $stringBuilder")

                val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
                textViewClubTable.text = stringBuilder.toString()

                val searchEditText = findViewById<EditText>(R.id.searchEditText)
                val searchButton = findViewById<Button>(R.id.searchButton)
                searchButton.setOnClickListener {
                    val searchQuery = searchEditText.text.toString().trim()
                    if (searchQuery.isNotEmpty()) {
                        val searchResult = dbHelper.searchClub(login, searchQuery)
                        if (searchResult.isNotEmpty()) {
                            val searchResultBuilder = StringBuilder()
                            for (club in searchResult) {
                                searchResultBuilder.append("Club Name: ${club.clubName}\n")
                                searchResultBuilder.append("Country: ${club.country}\n")
                                searchResultBuilder.append("Manager Name: ${club.managerName}\n")
                                searchResultBuilder.append("Rating: ${club.clubRating}\n\n")
                            }
                            textViewClubTable.text = searchResultBuilder.toString()
                        } else {
                            textViewClubTable.text = "No clubs found matching the search query."
                        }
                    } else {
                        Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
                textViewClubTable.text = "No clubs found for this login."
            }

            cursor?.close()
            dbHelper.close()
        }
    }
}
