package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LeaderBoardActivity : TranslateActivity() {
    private val firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference : DatabaseReference = firebase.getReference("leaderboard")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_leaderboard)

        // Find all Tvs for leaderboard
        val player1Tv : TextView = findViewById(R.id.first_player)
        val player2Tv : TextView = findViewById(R.id.second_player)
        val player3Tv : TextView = findViewById(R.id.third_player)
        val player4Tv : TextView = findViewById(R.id.fourth_player)

        val homeBtn : Button = findViewById(R.id.to_home)
        val settingsBtn : Button = findViewById(R.id.to_settings)
        val upgradeBtn : Button = findViewById(R.id.to_upgrades)
        val leaderBtn : Button = findViewById(R.id.to_leaderboard)

        val leaderboardListener = DataListener( listOf(player1Tv, player2Tv, player3Tv, player4Tv) )
        reference.addValueEventListener(leaderboardListener)

        homeBtn.setOnClickListener {
            val intent = Intent( this, MainActivity::class.java )
            startActivity(intent)
        }

        settingsBtn.setOnClickListener {
            val intent = Intent( this, SettingsActivity::class.java )
            startActivity(intent)
        }

        upgradeBtn.setOnClickListener {
            val intent = Intent( this, UpgradeActivity::class.java )
            startActivity(intent)
        }

        leaderBtn.setOnClickListener {
            val intent = Intent( this, LeaderBoardActivity::class.java)
            startActivity(intent)
        }
    }

    inner class DataListener(private val textViews: List<TextView>) : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.w("LeaderboardActivity", "Inside onDataChange")
            val leaderboard = mutableListOf<Pair<String, Int>>()
            for (entry in snapshot.children) {
                val name = entry.child("name").value.toString()
                val wave = entry.child("wave").value.toString().toIntOrNull() ?: 0
                leaderboard.add(Pair(name, wave))
            }
            // Sort leaderboard and update UI
            leaderboard.sortByDescending { it.second }
            for (i in textViews.indices) {
                val entry = leaderboard.getOrNull(i)
                textViews[i].text = if (entry != null) {
                    "${entry.first} - ${entry.second} Waves"
                } else {
                    " N/A - 0 Waves"
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("LeaderboardActivity", "Error fetching leaderboard: ${error.message}")
        }
    }

}