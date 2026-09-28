package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load default fragment (CalendarFragment)
        if (savedInstanceState == null) {
            loadFragment(CalendarFragment()) // Only load if it's the first time (to avoid reloads on orientation changes)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_calendar -> loadFragment(CalendarFragment())
                R.id.nav_calculator -> loadFragment(CalculatorFragment())
                R.id.nav_converter -> loadFragment(ConverterFragment())
                R.id.nav_flash_events -> loadFragment(FlashEventsFragment())
                R.id.nav_more -> loadFragment(MoreFragment()) // Load "More" fragment
            }
            true
        }
    }

    // Update parameter type to Fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Inflating the overflow menu (Settings, Help)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handling overflow menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                openSettings()
                true
            }
            R.id.menu_help -> {
                showHelpDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Open Settings Activity
    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    // Show Help Dialog
    private fun showHelpDialog() {
        val helpMessage = "Here you can find some help and useful tips."
        AlertDialog.Builder(this)
            .setTitle("Help")
            .setMessage(helpMessage)
            .setPositiveButton("OK", null)
            .show()
    }
}
