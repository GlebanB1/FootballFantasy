package com.example.footballfantasy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        val login = intent.getStringExtra("login")
        Log.d("GameActivity", "Login: $login")
        if (login != null) {
            val dbHelper = DataBaseHandler(this)

            val cursor = dbHelper.getClubTableData(login)

            Log.d("GameActivity", "Cursor is null or empty: ${cursor == null || cursor.count == 0}")

            if (cursor != null && cursor.moveToFirst()) {
                val stringBuilder = StringBuilder()

                val colClubName = dbHelper.COL_CLUB_NAME
                val colCountry = dbHelper.COL_COUNTRY
                val colManagerName = dbHelper.COL_MANAGER_NAME
                val colClubRating = dbHelper.COL_CLUB_RATING

                do {
                    val clubName = cursor.getString(cursor.getColumnIndex(colClubName))
                    val country = cursor.getString(cursor.getColumnIndex(colCountry))
                    val managerName = cursor.getString(cursor.getColumnIndex(colManagerName))
                    val rating = cursor.getString(cursor.getColumnIndex(colClubRating))

                    stringBuilder.append("Club Name: $clubName\n")
                    stringBuilder.append("Country: $country\n")
                    stringBuilder.append("Manager Name: $managerName\n")
                    stringBuilder.append("Rating: $rating\n\n")

                } while (cursor.moveToNext())

                Log.d("GameActivity", "Club table data: $stringBuilder")

                val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
                textViewClubTable.text = stringBuilder.toString()
            } else {
                val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
                textViewClubTable.text = "No clubs found for this login."
            }

            cursor?.close()
            dbHelper.close()
        }
    }
}
