package com.example.training2

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import android.content.SharedPreferences
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.material.switchmaterial.SwitchMaterial
import java.lang.Exception
import java.lang.NumberFormatException


class SettingsActivity : AppCompatActivity() {

    var isDark = false
    lateinit var editText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        editText = findViewById<EditText>(R.id.editTextNumber)
        editText.clearFocus()
        isDark = intent.getBooleanExtra("isDark", false)
        val sw1 = findViewById<Switch>(R.id.switch1)
        sw1.isChecked = isDark
        sw1?.setOnCheckedChangeListener { _, _ ->
            toggleDarkMode()
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    fun toggleDarkMode() {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
            .putBoolean("isDark", !isDark).apply()
        Toast.makeText(
            applicationContext,
            "Dark mode will update on next launch",
            Toast.LENGTH_SHORT
        ).show()
        if (!isDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        isDark = !isDark

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        var nb = 0
        try {
            nb = editText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            // handler
        } finally {
            if (nb>0)
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                .putInt("locationRefreshTime", nb).apply()
        }

    }

}