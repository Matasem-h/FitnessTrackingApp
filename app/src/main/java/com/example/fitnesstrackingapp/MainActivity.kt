package com.example.fitnesstrackingapp

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import android.os.Bundle
import android.content.Intent
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat

import com.example.fitnesstrackingapp.DataInputActivity
import com.example.fitnesstrackingapp.ProgressViewActivity
import com.example.fitnesstrackingapp.SettingsActivity

import android.view.Gravity
import android.util.TypedValue
import android.graphics.Color
import android.widget.LinearLayout
import org.threeten.bp.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.top_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor= ContextCompat.getColor(this, R.color.top_toolbar)

        // Navigation button setup
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show()
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
        // Weekly Goal update based on user input
        val editGoalButton = findViewById<Button>(R.id.edit_goal_button)
        val weeklyGoalText = findViewById<TextView>(R.id.weekly_goal_text)

        // Edit Goal Button Setup
        editGoalButton.setOnClickListener {
            val inputField = EditText(this)
            inputField.hint = "Enter your weekly goal"

            AlertDialog.Builder(this)
                .setTitle("Edit Weekly Goal")
                .setView(inputField)
                .setPositiveButton("Save") { _, _ ->
                    val userInput = inputField.text.toString()
                    if (userInput.isNotBlank()) {
                        weeklyGoalText.text = "Weekly Goal: $userInput"
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }










    }

    // Enable navigation icon response to clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}
