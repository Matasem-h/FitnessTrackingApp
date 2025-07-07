package com.example.fitnesstrackingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter

import android.widget.EditText
import android.app.DatePickerDialog
import android.widget.Button
import android.widget.Toast
import java.time.Year
import java.util.*
import android.content.Intent

class DataInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_input)

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
                    val formatted = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                    dateInput.setText(formatted)
                },
                year, month, day)

            datePickerDialog.show()
        }

        // Setup for Submit Button
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val exercise = exerciseInput.text.toString()
            val date = findViewById<EditText>(R.id.date_input).text.toString()
            val details = findViewById<EditText>(R.id.duration_input).text.toString()

            if (exercise.isNotBlank() && date.isNotBlank() && details.isNotBlank()) {
                // Save the data below here
                Toast.makeText(this, "Exercise logged for $date", Toast.LENGTH_SHORT).show()
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
}