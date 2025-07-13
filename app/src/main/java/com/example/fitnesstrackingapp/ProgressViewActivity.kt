package com.example.fitnesstrackingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import data.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate

//import org.w3c.dom.Text
//import org.w3c.dom.Text
//import java.time.LocalDate


class ProgressViewActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private val textViewMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_display)

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        // Loop for generating squares with date IDs
        val container = findViewById<LinearLayout>(R.id.week_grid_container)
        val today = LocalDate.now()
        val startDate = LocalDate.of(today.year, 6, 1)
        var current = startDate

        var currentMonth = ""

        fun getDaySuffix(day: Int): String {
            return when {
                day in 11..13 -> "${day}th"
                day % 10 == 1 -> "${day}st"
                day % 10 == 2 -> "${day}nd"
                day % 10 == 3 -> "${day}rd"
                else -> "${day}th"
            }
        }

        while (!current.isAfter(today)) {
            // Start new week row
            val weekLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            // Add month label only once per row (left column)
            val monthLabel = TextView(this).apply {
                text = current.month.toString().lowercase().replaceFirstChar { it.uppercase() }.take(3)
                width = 60 // Changed from 40
                height = 48
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            }
            weekLayout.addView(monthLabel)

            for (i in 0..6) {
                if (current.isAfter(today)) break

                val squareId = "square_${current.toString().replace("-", "_")}"
                val textView = TextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(0, 48).apply {
                        weight = 1f
                        setMargins(4, 4, 4, 4)
                    }
                    gravity = Gravity.CENTER
                    text = getDaySuffix(current.dayOfMonth)
                    setBackgroundColor(Color.LTGRAY)
                    id = View.generateViewId()
                    tag = squareId
                    val capturedDate = current
                    setOnClickListener {
                        Toast.makeText(this@ProgressViewActivity, "Selected date: $capturedDate", Toast.LENGTH_SHORT).show()
                    }
                }

                weekLayout.addView(textView)
                textViewMap[current.toString()] = textView.id
                current = current.plusDays(1)
            }

            // Fill remaining days in the last week with empty placeholders
            val remaining = 7 - weekLayout.childCount + 1
            for (j in 1..remaining ) {
                val placeholder = TextView(this).apply{
                    layoutParams = LinearLayout.LayoutParams(0, 48).apply {
                        weight = 1f
                        setMargins(4, 4, 4, 4)
                    }
                    setBackgroundColor(Color.TRANSPARENT)
                }
                weekLayout.addView(placeholder)
            }

            container.addView(weekLayout)
        }


        // Coroutine part
        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseProvider.getDatabase(applicationContext)
            val entries = db.exerciseDao().getAllExercises()
            val datesWithEntries = entries.map {it.date}
            val dateGrouped = entries.groupBy { it.date }

            withContext(Dispatchers.Main) {
                dateGrouped.forEach() { (dateStr, list) ->

                    val square = container.findViewWithTag<TextView>("square_${dateStr.replace("-","_")}")
                    square?.let {
                        val totalAmount = list.sumOf {
                            it.durationOrSets.toIntOrNull() ?:0
                        }
                        val colorRes = when {
                            totalAmount >= 15 -> R.color.goal_box_high
                            totalAmount >= 10 -> R.color.goal_box_medium
                            else -> R.color.goal_box_light
                        }
                        square.setBackgroundColor(ContextCompat.getColor(this@ProgressViewActivity, colorRes))
                    }
                }
            }
        }
    }

    // Enable navigation icon response to clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}