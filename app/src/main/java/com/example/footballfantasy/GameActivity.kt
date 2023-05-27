package com.example.footballfantasy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        val login = intent.getStringExtra("login")
        if (login != null) {
            val dbHelper = DataBaseHandler(this)
            val cursor = dbHelper.getClubTableData(login)

            // Process the cursor and display the club table data
            if (cursor != null && cursor.moveToFirst()) {
                val stringBuilder = StringBuilder()

                do {
                    val clubName = cursor.getString(cursor.getColumnIndex(COL_CLUB_NAME))
                    val country = cursor.getString(cursor.getColumnIndex(COL_COUNTRY))
                    val managerName = cursor.getString(cursor.getColumnIndex(COL_MANAGER_NAME))
                    val color = cursor.getString(cursor.getColumnIndex(COL_COLOR))

                    stringBuilder.append("Club Name: $clubName\n")
                    stringBuilder.append("Country: $country\n")
                    stringBuilder.append("Manager Name: $managerName\n")
                    stringBuilder.append("Color: $color\n\n")
                } while (cursor.moveToNext())

                // Assuming you have a TextView named 'textViewClubTable' in your layout XML file
                val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
                textViewClubTable.text = stringBuilder.toString()
            } else {
                val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
                textViewClubTable.text = "No clubs found for this login."
            }

            // Close the cursor and database connection after use
            cursor?.close()
            dbHelper.close()
        }
    }
}
