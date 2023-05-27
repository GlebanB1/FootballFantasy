package com.example.footballfantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        val dbHelper = DataBaseHandler(this)
        val cursor = dbHelper.getClubTableData()

        if (cursor != null && cursor.moveToFirst()) {
            val stringBuilder = StringBuilder()
            val clubNameIndex = cursor.getColumnIndex(COL_CLUB_NAME)
            val countryIndex = cursor.getColumnIndex(COL_COUNTRY)
            val managerNameIndex = cursor.getColumnIndex(COL_MANAGER_NAME)
            val colorIndex = cursor.getColumnIndex(COL_COLOR)

            do {
                val clubName = cursor.getString(clubNameIndex)
                val country = cursor.getString(countryIndex)
                val managerName = cursor.getString(managerNameIndex)
                val color = cursor.getString(colorIndex)

                stringBuilder.append("Club Name: $clubName\n")
                stringBuilder.append("Country: $country\n")
                stringBuilder.append("Manager Name: $managerName\n")
                stringBuilder.append("Color: $color\n\n")
            } while (cursor.moveToNext())

            // Assuming you have a TextView named 'textViewClubTable' in your layout XML file
            val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable)
            textViewClubTable.text = stringBuilder.toString()
        }

        // Close the cursor and database connection after use
        cursor?.close()
        dbHelper.close()
    }
}
