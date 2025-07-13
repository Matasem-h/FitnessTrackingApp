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
import android.widget.LinearLayout
import org.threeten.bp.LocalDate
import android.util.TypedValue
import android.graphics.Color

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // Past 7 days
        val squareContainer = findViewById<LinearLayout>(R.id.calendar_square_container)
        val db = data.DatabaseProvider.getDatabase(applicationContext)
        val today = LocalDate.now()

        CoroutineScope(Dispatchers.IO).launch {
            val entries = db.exerciseDao().getAllExercises()
            val dateGrouped = entries.groupBy { it.date }

            withContext(Dispatchers.Main) {
                for (i in 6 downTo 0) {
                    val date = today.minusDays(i.toLong())
                    val dateStr = date.toString()
                    val day = date.dayOfMonth

                    val totalAmount = dateGrouped[dateStr]?.sumOf {
                        getWeightedValue(it)
                    } ?: 0

                    val colorRes = when {
                        totalAmount >= 15 -> R.color.goal_box_high
                        totalAmount >= 10 -> R.color.goal_box_medium
                        totalAmount > 0 -> R.color.goal_box_light
                        else -> android.R.color.darker_gray
                    }

                    val square = TextView(this@MainActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(0, 48, 1f).apply {
                            setMargins(4, 4, 4, 4)
                        }
                        gravity = Gravity.CENTER
                        text = getDaySuffix(day)
                        setBackgroundColor(ContextCompat.getColor(this@MainActivity, colorRes))
                    }

                    squareContainer.addView(square)
                }
            }
        }

        // Show more button functionality
        val showMoreButton = findViewById<Button>(R.id.show_more_button)
        showMoreButton.setOnClickListener {
            startActivity(Intent(this, ProgressViewActivity::class.java))
        }
    }
    // Simple logic for day coloring
    private fun getWeightedValue(entry: data.ExerciseEntry): Int {
        val base = entry.durationOrSets.toIntOrNull() ?: 0
        return when (entry.name.lowercase()) {
            "push-ups", "sit-ups" -> base * 1         // Reps
            "walking", "cycling" -> base / 5          // Minutes
            "swimming" -> base / 5                     // Swimming is more intense
            else -> base
        }
    }

    // Days as dates
    private fun getDaySuffix(day: Int): String {
        return when {
            day in 11..13 -> "${day}th"
            day % 10 == 1 -> "${day}st"
            day % 10 == 2 -> "${day}nd"
            day % 10 == 3 -> "${day}rd"
            else -> "${day}th"
        }
    }

    // Enable navigation icon response to clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}
