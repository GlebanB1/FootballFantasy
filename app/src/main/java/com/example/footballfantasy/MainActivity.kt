package com.example.footballfantasy

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.widget.ProgressBar

// Riyad Ilyasov 211RDB371 Glebs Bondarevs 211RDB375

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        var progressBar = findViewById(R.id.progressBar) as ProgressBar
        progressBar.max=1000

        val currentProgress = 1000

        ObjectAnimator.ofInt(progressBar,"progress",currentProgress)
            .setDuration(3000)
            .start()

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)




    }
}