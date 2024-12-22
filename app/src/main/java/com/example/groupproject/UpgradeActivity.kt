package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class UpgradeActivity : TranslateActivity() {
    private var towerHealthUpgradeCost : Int = 10
    private var defenseHealthUpgradeCost : Int = 10
    private var enemySpeedUpgradeCost : Int = 10
    private var playerMoney : Int = 10

    // Player cash
    private lateinit var playerCash: TextView

    // curr stat TextViews (red text)
    private lateinit var currTowerHealth: TextView
    private lateinit var currDefenceHealth: TextView
    private lateinit var currEnemySpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_upgrade)

        // Load player money and btn costs
        playerMoney = SharedPreferencesManager.getInt("player_money", 0)
        towerHealthUpgradeCost = SharedPreferencesManager.getInt("tower_health_cost", 10)
        defenseHealthUpgradeCost = SharedPreferencesManager.getInt("defense_health_cost", 10)
        enemySpeedUpgradeCost = SharedPreferencesManager.getInt("enemy_speed_cost", 10)

        // Loading current tower stats
        val savedTowerHealth = SharedPreferencesManager.getInt("tower_max_health", 5)
        val savedDefenseHealth = SharedPreferencesManager.getInt("tower_defense_max_health", 1)
        val savedEnemySpeed = SharedPreferencesManager.getFloat("enemy_speed", 0.05f)

        // Menu btns
        val homeBtn : Button = findViewById(R.id.to_home)
        val settingsBtn : Button = findViewById(R.id.to_settings)
        val upgradeBtn : Button = findViewById(R.id.to_upgrades)
        val leaderBtn : Button = findViewById(R.id.to_leaderboard)

        // Find textViews
        playerCash = findViewById(R.id.cash_amount)
        currTowerHealth = findViewById(R.id.curr_tower_health)
        currDefenceHealth = findViewById(R.id.curr_defence_health)
        currEnemySpeed = findViewById(R.id.curr_enemy_speed)

        // Load textview values for stats
        currTowerHealth.text = savedTowerHealth.toString()
        currDefenceHealth.text = savedDefenseHealth.toString()
        currEnemySpeed.text = savedEnemySpeed.toString()

        // Upgrade btns
        val maxHealthBtn : Button = findViewById(R.id.upgrade_button_tower_health)
        val defenceHealthBtn : Button = findViewById(R.id.upgrade_button_defence_health)
        val enemySpeedBtn : Button = findViewById(R.id.upgrade_button_enemy_speed)

        // Update btns to show cost of upgrades
        maxHealthBtn.text = "$$towerHealthUpgradeCost"
        defenceHealthBtn.text = "$$defenseHealthUpgradeCost"
        enemySpeedBtn.text = "$$enemySpeedUpgradeCost"

        // Show players money
        updatePlayerMoneyDisplay()

        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setOnClickListener {
            val intent = Intent( this, SettingsActivity::class.java )
            startActivity(intent)
        }

        leaderBtn.setOnClickListener {
            val intent = Intent( this, LeaderBoardActivity::class.java)
            startActivity(intent)
        }

        maxHealthBtn.setOnClickListener {
            if(playerMoney >= towerHealthUpgradeCost) {
                playerMoney -= towerHealthUpgradeCost
                towerHealthUpgradeCost += 5

                val newTowerHealth = currTowerHealth.text.toString().toInt() + 2
                currTowerHealth.text = newTowerHealth.toString()
                SharedPreferencesManager.saveInt("player_money", playerMoney)

                // Save tower stats to shared data
                saveUpgradesToPreferences()

                // Save upgrade costs to shared data
                saveUpgradeCosts()

                // Update player money to show new amount after upgrade
                updatePlayerMoneyDisplay()

                maxHealthBtn.text = "$$towerHealthUpgradeCost"
                Toast.makeText(this, "Tower health upgraded!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not enough money!", Toast.LENGTH_SHORT).show()
            }
        }

        defenceHealthBtn.setOnClickListener {
            if (playerMoney >= defenseHealthUpgradeCost) {
                playerMoney -= defenseHealthUpgradeCost
                defenseHealthUpgradeCost += 5

                val newDefenseHealth = currDefenceHealth.text.toString().toInt() + 2
                currDefenceHealth.text = newDefenseHealth.toString()
                SharedPreferencesManager.saveInt("player_money", playerMoney)

                saveUpgradesToPreferences()
                saveUpgradeCosts()

                updatePlayerMoneyDisplay()
                defenceHealthBtn.text = "$$defenseHealthUpgradeCost"
                Toast.makeText(this, "Defense health upgraded!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not enough money!", Toast.LENGTH_SHORT).show()
            }
        }

        enemySpeedBtn.setOnClickListener {
            if (playerMoney >= enemySpeedUpgradeCost) {
                playerMoney -= enemySpeedUpgradeCost
                enemySpeedUpgradeCost += 5

                val newEnemySpeed = currEnemySpeed.text.toString().toFloat() + 0.05f
                currEnemySpeed.text = String.format("%.2f", newEnemySpeed)
                SharedPreferencesManager.saveInt("player_money", playerMoney)

                saveUpgradesToPreferences()
                saveUpgradeCosts()

                updatePlayerMoneyDisplay()
                enemySpeedBtn.text = "$$enemySpeedUpgradeCost"
                Toast.makeText(this, "Enemy speed decreased!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not enough money!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePlayerMoneyDisplay() {
        playerCash.text = "$$playerMoney"
    }

    fun saveUpgradeCosts() {
        SharedPreferencesManager.saveInt("tower_health_cost", towerHealthUpgradeCost)
        SharedPreferencesManager.saveInt("defense_health_cost", defenseHealthUpgradeCost)
        SharedPreferencesManager.saveInt("enemy_speed_cost", enemySpeedUpgradeCost)
    }

    fun saveUpgradesToPreferences() {
        SharedPreferencesManager.saveInt("tower_max_health", currTowerHealth.text.toString().toInt())
        SharedPreferencesManager.saveInt("tower_defense_max_health", currDefenceHealth.text.toString().toInt())
        SharedPreferencesManager.saveFloat("enemy_speed", currEnemySpeed.text.toString().toFloat())
    }
}