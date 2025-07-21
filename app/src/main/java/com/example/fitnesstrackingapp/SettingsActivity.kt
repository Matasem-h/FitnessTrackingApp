package com.example.fitnesstrackingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
        // Reset button functionality
        val resetButton = findViewById<Button>(R.id.reset_button)

        resetButton.setOnClickListener {
            // Show confirmation dialog
            AlertDialog.Builder(this)
                .setTitle("Reset Progress")
                .setMessage("Are you sure you want to reset all your progress?")
                .setPositiveButton("Yes") {_, _ ->
                    // Run reset in background
                    CoroutineScope(Dispatchers.IO).launch {
                        // Access the database and delete all exercise entries
                        val db = data.DatabaseProvider.getDatabase(applicationContext)
                        db.exerciseDao().deleteAll() // Delete all entries

                        // Reset Weekly Goal
                        val sharedPrefs = getSharedPreferences("FitnessAppsPrefs", MODE_PRIVATE)
                        sharedPrefs.edit().clear().apply()

                        withContext(Dispatchers.Main) {
                            // Notify the user that all exercise data has been deleted
                            Toast.makeText(this@SettingsActivity, "All progress has been reset.", Toast.LENGTH_SHORT).show()

                            // Restart app to reflect reset changes
                            val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                }
                .setNegativeButton("No", null) // Do nothing on cancel
                .show()
        }
    }

    // Enable navigation icon response to clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}