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
        
    }
}