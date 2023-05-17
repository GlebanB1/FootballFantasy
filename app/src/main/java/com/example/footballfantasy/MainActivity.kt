package com.example.footballfantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}