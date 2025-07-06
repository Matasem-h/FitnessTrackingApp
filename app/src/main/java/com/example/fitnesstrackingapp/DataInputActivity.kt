package com.example.fitnesstrackingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter

class DataInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_input)

        val exerciseInput = findViewById<AutoCompleteTextView>(R.id.exercise_input)

        val exercises = listOf("Walking", "Swimming", "Cycling", "Push-Ups", "Sit-Ups")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, exercises)

        exerciseInput.setAdapter(adapter)
    }
}