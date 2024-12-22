package com.example.groupproject

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

open class TranslateActivity : AppCompatActivity() {
    private lateinit var translator : AppTranslator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val targetLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"
        translator = AppTranslator(this)
        translator.initializeTranslator(targetLanguage)

        translator.downloadModel(
            onSuccess = { translateStrings() },
            onFailure = { error -> println("Error downloading language model: $error") }
        )
    }

    private fun translateStrings() {
        // Map ids with corresponding strings
        val stringsToTranslate = mapOf(
            // Home screen btns
            R.id.start_btn to getString(R.string.startBtn),
            R.id.nameValue to getString(R.string.player_name) + " " +
                    SharedPreferencesManager.getString("player_name", "  "),

            // Bottom menu btns ( all activities )
            R.id.to_home to getString(R.string.homeBtn),
            R.id.to_upgrades to getString(R.string.upgradesBtn),
            R.id.to_leaderboard to getString(R.string.leadersBtn),
            R.id.to_settings to getString(R.string.settingsBtn),

            // Upgrade screen btns
            R.id.upgrade_header to getString(R.string.upgrades_header),
            R.id.cash to getString(R.string.cash),
            R.id.upgrade_tower_health to getString(R.string.tower_health),
            R.id.upgrade_defence_health to getString(R.string.defence_health),
            R.id.upgrade_enemies_speed to getString(R.string.enemy_speed),

            // Game screen btns (UPDATED NO LONGER HAVE HOME BTN IN GAME)
            //R.id.game_Home to getString(R.string.end_game),

            // Leaderboard screen btns
            R.id.leader_header to getString(R.string.leader_board),

            // Settings screen btns
            R.id.settings_tv to getString(R.string.settings_header),
            R.id.player_name_tv to getString(R.string.player_name),
            R.id.name_hint to getString(R.string.enter_name_here),
            R.id.set_name to getString(R.string.save_name),
            R.id.reset_stats to getString(R.string.reset_game_stats)
        )

        // Translate each string and update the corresponding UI component
        stringsToTranslate.forEach { (viewId, originalText) ->
            translator.translateText(
                inputText = originalText,
                onSuccess = { translatedText ->
                    val view = findViewById<View>(viewId)
                    // Get view type for id
                    when (view) {
                        is TextView -> view.text = translatedText
                        is Button -> view.text = translatedText
                        is EditText -> view.hint = translatedText
                        is Switch -> view.text = translatedText
                        else -> println("Unsupported view type for ID: $viewId")
                    }
                },
                // Set original text on failure
                onFailure = { error ->
                    println("Translation failed for $viewId: $error")
                    val view = findViewById<View>(viewId)
                    when (view) {
                        is TextView -> view.text = originalText
                        is Button -> view.text = originalText
                        is EditText -> view.hint = originalText
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        translator.closeTranslator()
    }
}