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

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}