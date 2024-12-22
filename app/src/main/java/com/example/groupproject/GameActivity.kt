package com.example.groupproject

import android.R
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Timer

class GameActivity : TranslateActivity()  {
    private lateinit var gameView: GameView
    private lateinit var game: Game
    private var isRoundOver = false

    private val firebase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = firebase.getReference("leaderboard")

    private lateinit var towerHealthBar : ProgressBar
    private lateinit var defenseHealthBar : ProgressBar
    private lateinit var towerHealthText : TextView
    private lateinit var defenseHealthText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            buildViewByCode()
            animate()
        }
    }

    private fun buildViewByCode() {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        val rectangle = Rect(0, 0, 0, 0)
        window.decorView.getWindowVisibleDisplayFrame(rectangle)

        // Load upgrades from SharedPreferences
        val savedTowerHealth = SharedPreferencesManager.getInt("tower_max_health", 5)
        val savedDefenseHealth = SharedPreferencesManager.getInt("tower_defense_max_health", 1)
        val savedEnemySpeed = SharedPreferencesManager.getFloat("enemy_speed", 0.05f)

        val frameLayout = FrameLayout(this)

        gameView = GameView(this, width, height - rectangle.top, savedTowerHealth, savedDefenseHealth, savedEnemySpeed)
        game = gameView.getGame()

        frameLayout.addView(gameView)

        // Set up tower health bars
        setupHealthBars(frameLayout, savedTowerHealth, savedDefenseHealth)

        setContentView(frameLayout)
    }

    private fun animate() {
        var timer = Timer( )
        var task = GameTimerTask( this )
        timer.schedule( task, 0L, GameView.DELTA_TIME.toLong() )
    }

    fun updateModel() {
        if(game.isRunning()) {
            game.moveEnemiesTowardsTower()
            updateHealthBars()
            if(game.getEnemies().isEmpty()) {
                game.startWave()
                runOnUiThread {
                    Toast.makeText(this, "Wave ${game.getWave()}", Toast.LENGTH_SHORT).show()
                }
            }
        // Only run once!
        } else if (!isRoundOver) {
            isRoundOver = true
            val finalWave: Int = game.getWave() - 1
            updateLeaderboard( finalWave )
            val storedMoney = SharedPreferencesManager.getInt( "player_money", 0 )
            SharedPreferencesManager.saveInt( "player_money", storedMoney + game.getMoney() )
            Log.d("GameActivity", "Final stored money: ${storedMoney + game.getMoney()}")
            finish()
        }
    }

    fun updateView() {
        gameView.postInvalidate()
    }

    fun updateLeaderboard( waveNumber: Int ) {
        val sharedPreferences = getSharedPreferences( "AppPreferences", MODE_PRIVATE )
        val playerName = sharedPreferences.getString( "player_name", "Unknown Player" ) ?: "Unknown Player"

        reference.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange( snapshot: DataSnapshot ) {
                val leaderboard = mutableListOf<Pair<String, Int>>()

                // Read current leaderboard data
                for ( entry in snapshot.children ) {
                    val name = entry.child( "name" ).value.toString()
                    val wave = entry.child( "wave" ).value.toString().toIntOrNull() ?: 0
                    leaderboard.add(Pair( name, wave ) )
                }

                // Add the current player's name and wave to the rest read
                leaderboard.add(Pair( playerName, waveNumber ) )

                // Sort the leaderboard
                leaderboard.sortByDescending { it.second }

                // Take top 4 entries
                val topPlayers = leaderboard.take( 4 )

                val leaderboardData = topPlayers.mapIndexed { index, player ->
                    mapOf( "name" to player.first, "wave" to player.second )
                }

                // Write the top 4 players back to Firebase
                reference.setValue( leaderboardData ).addOnSuccessListener {
                    Log.d( "GameActivity", "Leaderboard updated successfully." )
                }.addOnFailureListener { e ->
                    Log.e( "GameActivity", "Failed to update leaderboard: ${e.message}" )
                }
            }

            override fun onCancelled( error: DatabaseError ) {
                Log.e( "GameActivity", "Failed to get leaderboard: ${error.message}" )
            }
        })
    }

    private fun setupHealthBars( frameLayout: FrameLayout, maxTowerHealth: Int, maxDefenseHealth: Int ) {
        // Tower Health ProgressBar
         towerHealthBar =
            ProgressBar( this, null, R.attr.progressBarStyleHorizontal ).apply {
                max = maxTowerHealth
                progress = maxTowerHealth
                isIndeterminate = false
            }

        // Defense Health ProgressBar
        defenseHealthBar =
            ProgressBar( this, null, R.attr.progressBarStyleHorizontal ).apply {
                max = maxDefenseHealth
                progress = maxDefenseHealth
                isIndeterminate = false
            }

        // TextViews for tower and defense Health
        towerHealthText = TextView( this ).apply {
            text = "Tower Health: $maxTowerHealth/$maxTowerHealth"
            gravity = Gravity.CENTER
        }

        defenseHealthText = TextView( this ).apply {
            text = "Defense Health: $maxDefenseHealth/$maxDefenseHealth"
            gravity = Gravity.CENTER
        }

        // Tower Health LayoutParams
        val towerTextParams = FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT ).apply {
            gravity = Gravity.TOP
            setMargins( 50, 50, 50, 0 )
        }

        val towerBarParams = FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.TOP
            setMargins( 50, 100, 50, 0 )
        }

        // Defense Health LayoutParams
        val defenseTextParams = FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT ).apply {
            gravity = Gravity.TOP
            setMargins( 50, 200, 50, 0 )
        }

        val defenseBarParams = FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT ).apply {
            gravity = Gravity.TOP
            setMargins( 50, 250, 50, 0 )
        }

        frameLayout.addView( towerHealthText, towerTextParams )
        frameLayout.addView( towerHealthBar, towerBarParams )
        frameLayout.addView( defenseHealthText, defenseTextParams )
        frameLayout.addView( defenseHealthBar, defenseBarParams )
    }

    private fun updateHealthBars() {
        val towerHealth = game.getTowerHealth()
        val towerMaxHealth = game.getTowerMaxHealth()
        val defenseHealth = game.getDefenseHealth()
        val defenseMaxHealth = game.getDefenseMaxHealth()

        towerHealthBar.progress = towerHealth
        towerHealthText.text = "Tower Health: $towerHealth/$towerMaxHealth"

        defenseHealthBar.progress = defenseHealth
        defenseHealthText.text = "Defense Health: $defenseHealth/$defenseMaxHealth"
    }
}