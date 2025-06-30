package com.example.fitnesstrackingapp

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import android.os.Bundle
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.content.ContextCompat
import java.io.DataInput
import com.example.fitnesstrackingapp.DataInputActivity
import com.example.fitnesstrackingapp.ProgressViewActivity
import com.example.fitnesstrackingapp.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.top_toolbar)
        setSupportActionBar(findViewById(R.id.top_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        // Navigation button setup
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Placing the navigation button on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation button functionality
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
    }
    // Enable navigation icon response to clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}
