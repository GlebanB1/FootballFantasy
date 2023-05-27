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

        etvLogin.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                // Hide the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etvLogin.windowToken, 0)

                val login = etvLogin.text.toString().trim()
                if (login.isNotEmpty()) {
                    val db = DataBaseHandler(this)
                    if (db.isLoginExists(login)) {
                        // Login exists, show a toast message
                        Toast.makeText(this, "Login already exists", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, StartGame::class.java)
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
                return@setOnEditorActionListener true
            }
            false
        }

        btnInsert.setOnClickListener {
            val login = etvLogin.text.toString().trim()
            if (login.isNotEmpty()) {
                val db = DataBaseHandler(this)
                if (db.isLoginExists(login)) {
                    // Login exists, show a toast message
                    Toast.makeText(this, "Login already exists", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, StartGame::class.java)
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

        val intent = Intent(this@LoginActivity, GameActivity::class.java)
        intent.putExtra("login", login)
        startActivity(intent)
    }


}
