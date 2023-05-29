package com.example.footballfantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast


class ClubCreation : AppCompatActivity() {

    private lateinit var dbHandler: DataBaseHandler
    private lateinit var login: String
    private lateinit var clubNameEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var managerNameEditText: EditText
    private lateinit var clubRatingEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_creation)
        supportActionBar?.hide()

        // Initialize the database handler
        dbHandler = DataBaseHandler(this)

        // Retrieve login from intent extra
        val sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE)
        login = sharedPreferences.getString("login", "") ?: ""

        // Initialize the EditText views
        clubNameEditText = findViewById(R.id.eclubname)
        countryEditText = findViewById(R.id.ecountry)
        managerNameEditText = findViewById(R.id.emanager)
        clubRatingEditText = findViewById(R.id.erating)

        // Set click listener for the insertClubButton
        val insertClubButton = findViewById<Button>(R.id.insertClubButton)
        insertClubButton.setOnClickListener { insertClub() }
    }

    private fun insertClub() {
        val clubName = clubNameEditText.text.toString().trim()
        val country = countryEditText.text.toString().trim()
        val managerName = managerNameEditText.text.toString().trim()
        val clubRating = clubRatingEditText.text.toString().trim()

        if (clubName.isNotEmpty() && country.isNotEmpty() && managerName.isNotEmpty() && clubRating.isNotEmpty()) {
            val footballClub = FootballClub(clubName, country, managerName, clubRating)
            val result = dbHandler.insertClub(login, footballClub)
            if (result != -1L) {
                Toast.makeText(this, "Club inserted successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        clubNameEditText.text.clear()
        countryEditText.text.clear()
        managerNameEditText.text.clear()
        clubRatingEditText.text.clear()
    }
}
