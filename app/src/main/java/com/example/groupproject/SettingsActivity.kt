package com.example.groupproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class SettingsActivity : TranslateActivity() {
    private var isSpinnerInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val languageSelect: Spinner = findViewById(R.id.language_select)
        val languageCodes = arrayOf("en", "es")

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en")

        val saveNameBtn: Button = findViewById(R.id.set_name)
        val playerNameEditText: EditText = findViewById(R.id.name_hint)
        val homeBtn: Button = findViewById(R.id.to_home)
        val settingsBtn: Button = findViewById(R.id.to_settings)
        val upgradeBtn: Button = findViewById(R.id.to_upgrades)
        val leaderBtn: Button = findViewById(R.id.to_leaderboard)
        val resetBtn : Button = findViewById(R.id.reset_stats)

        languageSelect.setSelection(languageCodes.indexOf(savedLanguage))
        isSpinnerInitialized = true

        languageSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSpinnerInitialized) {
                    val selectedLanguage = languageCodes[position]
                    if (selectedLanguage != savedLanguage) {
                        saveSelectedLanguage(sharedPreferences, selectedLanguage)
                        restartApp()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        upgradeBtn.setOnClickListener {
            val intent = Intent(this, UpgradeActivity::class.java)
            startActivity(intent)
        }

        leaderBtn.setOnClickListener {
            val intent = Intent(this, LeaderBoardActivity::class.java)
            startActivity(intent)
        }

        saveNameBtn.setOnClickListener {
            val playerName = playerNameEditText.text.toString().trim()
            if (playerName.isNotEmpty()) {
                savePlayerName(sharedPreferences, playerName)
                Toast.makeText(this, "Player name saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid name.", Toast.LENGTH_SHORT).show()
            }
        }

        resetBtn.setOnClickListener{
            SharedPreferencesManager.clearAll()
        }
    }

    private fun saveSelectedLanguage(sharedPreferences: SharedPreferences, languageCode: String) {
        sharedPreferences.edit().putString("selected_language", languageCode).apply()
    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
    }

    private fun savePlayerName(sharedPreferences: SharedPreferences, playerName: String) {
        sharedPreferences.edit().putString("player_name", playerName).apply()
        Log.w("NameSave", "Saved player name: $playerName")
    }
}

