package com.example.footballfantasy
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MatchActivity : AppCompatActivity() {

    private lateinit var login: String
    private lateinit var dbHelper: DataBaseHandler

    private lateinit var spinnerTeam1: Spinner
    private lateinit var spinnerTeam2: Spinner
    private lateinit var textViewMatchResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        supportActionBar?.hide()

        login = intent.getStringExtra("login") ?: ""
        dbHelper = DataBaseHandler(this)
        spinnerTeam1 = findViewById(R.id.spinnerTeam1)
        spinnerTeam2 = findViewById(R.id.spinnerTeam2)
        textViewMatchResult = findViewById(R.id.textViewMatchResult)

        val clubList = dbHelper.getClubsByLogin(login)

        if (clubList.isEmpty()) {
            textViewMatchResult.text = "No clubs found for this login."
            return
        }

        val clubNames = clubList.map { it.clubName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clubNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTeam1.adapter = adapter
        spinnerTeam2.adapter = adapter
    }

    fun simulateMatch(view: View) {
        val team1 = spinnerTeam1.selectedItem.toString()
        val team2 = spinnerTeam2.selectedItem.toString()

        if (team1 == team2) {
            textViewMatchResult.text = "Cannot select the same team for both sides."
            return
        }

        val score1 = Random.nextInt(0, 5) // Random score for team 1 (0 to 4)
        val score2 = Random.nextInt(0, 5) // Random score for team 2 (0 to 4)

        val result = "$team1 $score1 : $score2 $team2"
        textViewMatchResult.text = result
    }
}
