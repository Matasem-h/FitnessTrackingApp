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
        // Day squares with ID for each
        val container = findViewById<LinearLayout>(R.id.week_grid_container)
        val today = LocalDate.now()

        for (i in 0 until 56 step 7) {
            val weekLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val startOfWeek = today.minusDays(i.toLong())
            val monthsLabel = TextView(this).apply {
                text = startOfWeek.month.name.take(3)
                width = 40
                height = 48
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            }
            weekLayout.addView(monthsLabel)
            for(j in 0..6) {
                val date = startOfWeek.minusDays(j.toLong())
                if (date.isAfter(today)) continue
                val squareId = "square_${date.toString().replace("-", "_")}"
                val textView = TextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(0, 48).apply {
                        weight = 1f
                        setMargins(4,4,4,4)
                    }
                    gravity = Gravity.CENTER
                    text = date.dayOfMonth.toString() // Optional Show Day number
                    setBackgroundColor(Color.LTGRAY)
                    id = View.generateViewId()
                    tag = squareId // Store data key as tag
                    // Adding click events for each square.
                    setOnClickListener {
                        Toast.makeText(this@ProgressViewActivity, "Selected date: $date", Toast.LENGTH_SHORT).show()
                    }
                }
                // Save mapping: id -> date for later (Optional)
                weekLayout.addView(textView)
                // Store generated view ID for lookup
                textViewMap[date.toString()] = textView.id
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
                        val count = list.size
                        val colorRes = when {
                            count >= 15 -> R.color.goal_box_high
                            count >= 10 -> R.color.goal_box_medium
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