package com.example.footballfantasy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
                val db = DataBaseHandler(this)
                if (db.isLoginExists(login)) {
                    // Login exists, show a toast message
                    Toast.makeText(this, "Login already exists", Toast.LENGTH_SHORT).show()
                    saveLogin(login) // Save the login using shared preferences
                    val intent = Intent(this@LoginActivity, StartGame::class.java)
                    intent.putExtra("login", login) // Pass the login value to StartGame activity
                    startActivity(intent)
                } else {
                    // Login doesn't exist, insert into table and show a toast message
                    val user = User(login)
                    val insertResult = db.insertData(user)
                    if (insertResult != -1L) {
                        saveLogin(login) // Save the login using shared preferences
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, ClubCreation::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failed to insert login", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a login", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun saveLogin(login: String) {
        val sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("login", login)
        editor.apply()
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
    }



}
