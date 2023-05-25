package com.example.footballfantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast


class ClubCreation : AppCompatActivity() {

    private lateinit var dbHandler: DataBaseHandler
    private lateinit var login: String
    private lateinit var clubNameEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var managerNameEditText: EditText
    private lateinit var colorSpinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_creation)

        dbHandler = DataBaseHandler(this)

        // Retrieve login from intent extra
        val sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE)
        login = sharedPreferences.getString("login", "") ?: ""


        clubNameEditText = findViewById(R.id.etvClubName)
        countryEditText = findViewById(R.id.etvCountry)
        managerNameEditText = findViewById(R.id.etvMgName)
        colorSpinner = findViewById(R.id.colorSpinner)


        val insertClubButton = findViewById<Button>(R.id.insertClubButton)
        insertClubButton.setOnClickListener { insertClub() }
    }

    private fun insertClub() {
        val clubName = clubNameEditText.text.toString().trim()
        val country = countryEditText.text.toString().trim()
        val managerName = managerNameEditText.text.toString().trim()
        val color = colorSpinner.selectedItem.toString()

        if (clubName.isEmpty() || country.isEmpty() || managerName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val club = FootballClub(clubName, country, managerName, color)
        val result = dbHandler.insertClub(login, club)

        if (result != -1L) {
            Toast.makeText(this, "Club inserted successfully", Toast.LENGTH_SHORT).show()
            clearFields()
        } else {
            Toast.makeText(this, "Failed to insert club", Toast.LENGTH_SHORT).show()
        }
    }


    private fun clearFields() {
        clubNameEditText.text.clear()
        countryEditText.text.clear()
        managerNameEditText.text.clear()
    }
}
