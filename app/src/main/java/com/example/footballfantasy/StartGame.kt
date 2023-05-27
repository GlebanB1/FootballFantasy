package com.example.footballfantasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)
        supportActionBar?.hide()

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnExit = findViewById<Button>(R.id.btnExit)
        val btnStart = findViewById<Button>(R.id.btnStart)

        btnEdit.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnStart.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}
