package com.example.footballfantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.hide()

        val button = findViewById(R.id.button) as Button

        button.setOnClickListener {
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }



    }
}