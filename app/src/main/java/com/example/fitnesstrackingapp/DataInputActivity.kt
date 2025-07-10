package com.example.fitnesstrackingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.app.DatePickerDialog
import android.widget.Button
import android.widget.Toast
import java.util.*
import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.time.Year

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import data.DatabaseProvider
import data.ExerciseEntry
import kotlinx.coroutines.withContext

class DataInputActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_input)

        // Navigation Button Setup
        setSupportActionBar(findViewById(R.id.top_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Placing the navigation button on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation button functionality
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.nav_input -> {
                    startActivity(Intent(this, DataInputActivity::class.java))
                }
                R.id.nav_progress -> {
                    startActivity(Intent(this, ProgressViewActivity::class.java))
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Page-Specific Code
        // Setup for Exercise Input
        val exerciseInput = findViewById<AutoCompleteTextView>(R.id.exercise_input)
        val exercises = listOf("Walking", "Swimming", "Cycling", "Push-Ups", "Sit-Ups")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, exercises)
        exerciseInput.setAdapter(adapter)

        // Setup for Date Picker
        val dateInput = findViewById<EditText>(R.id.date_input)
        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formatted = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    dateInput.setText(formatted)
                },
                year, month, day)

            datePickerDialog.show()
        }

        // Setup for Submit Button
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {

            val exercise = exerciseInput.text.toString()
            val duration = findViewById<EditText>(R.id.duration_input).text.toString()
            val date = dateInput.text.toString()

            if (exercise.isNotBlank() && duration.isNotBlank() && date.isNotBlank()) {
                // Save the data below here
                val entry = ExerciseEntry(name = exercise, durationOrSets = duration, date = date)

                CoroutineScope(Dispatchers.IO).launch {
                    val db = DatabaseProvider.getDatabase(applicationContext)
                    db.exerciseDao().insertExercise(entry) // Changes color of day square
                }

                // Show "Toast" meaning confirmation message on UI when submit button is pressed
                Toast.makeText(this, "Exercise Saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Setup for Exercise History Button
        val historyButton = findViewById<Button>(R.id.exercise_history_button)
        historyButton.setOnClickListener {
            startActivity(Intent(this, ProgressViewActivity::class.java))
        }
    }
    // Enable navigation icon response to clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}