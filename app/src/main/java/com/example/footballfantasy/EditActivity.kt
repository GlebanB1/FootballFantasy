package com.example.footballfantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class EditActivity : AppCompatActivity() {

    private lateinit var login: String
    private lateinit var dbHelper: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.hide()

        login = intent.getStringExtra("login") ?: ""
        dbHelper = DataBaseHandler(this)

        val cursor = dbHelper.getClubTableData(login)
        val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable1)
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

            textViewClubTable.text = stringBuilder.toString()
        } else {
            textViewClubTable.text = "No clubs found for this login."
        }

        cursor?.close()

        val btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            deleteAllClubsForLogin()
        }

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            editClub()
        }
    }

    private fun deleteClub(selectedClub: FootballClub) {
        val db = DataBaseHandler(this)
        db.deleteClub(login, selectedClub)
        Toast.makeText(this, "Club deleted successfully", Toast.LENGTH_SHORT).show()
        refreshClubTable()
    }

    private fun deleteAllClubsForLogin() {
        val clubList = dbHelper.getClubsByLogin(login)

        if (clubList.isEmpty()) {
            Toast.makeText(this, "No clubs found for this login.", Toast.LENGTH_SHORT).show()
            return
        }

        val clubNames = clubList.map { it.clubName }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a club to delete")

        builder.setItems(clubNames.toTypedArray()) { _, position ->
            val selectedClub = clubList[position]
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Delete Club")
            dialogBuilder.setMessage("Are you sure you want to delete ${selectedClub.clubName}?")

            dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                deleteClub(selectedClub)
                dialog.dismiss()
            }

            dialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun editClub() {
        val clubList = dbHelper.getClubsByLogin(login)

        if (clubList.isEmpty()) {
            Toast.makeText(this, "No clubs found for this login.", Toast.LENGTH_SHORT).show()
            return
        }

        val clubNames = clubList.map { it.clubName }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a club to edit")

        builder.setItems(clubNames.toTypedArray()) { _, position ->
            val selectedClub = clubList[position]
            val oldManagerName = selectedClub.managerName

            val inputLayout = EditText(this)
            inputLayout.setText(oldManagerName)

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Edit Manager Name")
            dialogBuilder.setView(inputLayout)

            dialogBuilder.setPositiveButton("Save") { dialog, _ ->
                val newManagerName = inputLayout.text.toString()

                if (newManagerName.isNotEmpty()) {
                    val newClub = selectedClub.copy(managerName = newManagerName)
                    dbHelper.updateClub(login, selectedClub, newClub)
                    Toast.makeText(this, "Manager name updated successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    refreshClubTable()
                } else {
                    Toast.makeText(this, "Please enter a valid manager name", Toast.LENGTH_SHORT).show()
                }
            }

            dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun refreshClubTable() {
        val cursor = dbHelper.getClubTableData(login)
        val textViewClubTable = findViewById<TextView>(R.id.textViewClubTable1)
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

            textViewClubTable.text = stringBuilder.toString()
        } else {
            textViewClubTable.text = "No clubs found for this login."
        }

        cursor?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
