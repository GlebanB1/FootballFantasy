package com.example.footballfantasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class StartGame : AppCompatActivity() {
    private var login: String? = null
    private lateinit var dbHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)
        supportActionBar?.hide()

        login = intent.getStringExtra("login") // Retrieve the login value from the intent
        dbHandler = DataBaseHandler(this)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnExit = findViewById<Button>(R.id.btnExit)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        btnEdit.setOnClickListener {
            if (login != null) {
                val clubs = dbHandler.getClubsByLogin(login!!)
                if (clubs.isNotEmpty()) {
                    val intent = Intent(this, EditActivity::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No clubs found for the login", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No login found", Toast.LENGTH_SHORT).show()
            }
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, ClubCreation::class.java)
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            finishAffinity();
            System.exit(0);
        }

        btnStart.setOnClickListener {
            if (login != null) {
                val clubs = dbHandler.getClubsByLogin(login!!)
                if (clubs.isNotEmpty()) {
                    val intent = Intent(this, GameActivity::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No clubs found for the login", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No login found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHandler.close()
    }
}
