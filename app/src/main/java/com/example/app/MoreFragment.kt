package com.example.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.widget.Toast

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        val btnChangeTheme: Button = view.findViewById(R.id.btn_change_theme)
        val btnHelp: Button = view.findViewById(R.id.btn_help)

        // Set listeners for the buttons
        btnChangeTheme.setOnClickListener { toggleTheme() }
        btnHelp.setOnClickListener { showHelp() }

        return view
    }

    // Toggle theme and save preference
    private fun toggleTheme() {
        val sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val currentMode = AppCompatDelegate.getDefaultNightMode()

        val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }

        sharedPreferences.edit().putInt("theme_mode", newMode).apply()
        AppCompatDelegate.setDefaultNightMode(newMode)

        val themeName = if (newMode == AppCompatDelegate.MODE_NIGHT_YES) "Dark Mode" else "Light Mode"
        Toast.makeText(activity, "Switched to $themeName", Toast.LENGTH_SHORT).show()
    }

    // Show help message with contact details
    private fun showHelp() {
        val helpMessage = """
            Need assistance?
            Contact our support team at:
            
            📞 +123-456-7890
            ✉️ support@example.com
        """.trimIndent()

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Help & Support")
            .setMessage(helpMessage)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Call Now") { _, _ ->
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = android.net.Uri.parse("tel:+1234567890")
                startActivity(callIntent)
            }
            .show()
    }
}
