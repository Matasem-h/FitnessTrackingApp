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
import androidx.core.content.ContextCompat
import data.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

import java.time.LocalDate

class ProgressViewActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

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

        // Coroutine part?
        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseProvider.getDatabase(applicationContext)
            val entries = db.exerciseDao().getAllExercises()
            val datesWithEntries = entries.map {it.date}

            withContext(Dispatchers.Main) {
                datesWithEntries.forEach() { dateStr ->


                    val viewId = resources.getIdentifier("square_${dateStr.replace("-", "_")}", "id", packageName)
                    val square = findViewById<TextView>(viewId)



                    square?.setBackgroundColor(ContextCompat.getColor(this@ProgressViewActivity, R.color.goal_box))
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