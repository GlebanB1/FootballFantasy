package com.example.footballfantasy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var btnInsert: Button
    private lateinit var etvLogin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btnInsert = findViewById(R.id.btnInsert)
        etvLogin = findViewById(R.id.etvLogin)


        btnInsert.setOnClickListener {
            val login = etvLogin.text.toString().trim()
            if (login.isNotEmpty()) {
                var user = User(etvLogin.text.toString())
                val db = DataBaseHandler(this)
                db.insertData(user)
            } else {
                Toast.makeText(this, "Please enter a login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
