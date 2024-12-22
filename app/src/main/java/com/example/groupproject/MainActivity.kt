package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class MainActivity : TranslateActivity() {
    private lateinit var nameValue : TextView
    private lateinit var moneyValue : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        SharedPreferencesManager.init(this)

        nameValue = findViewById(R.id.nameValue)
        nameValue.text = getString(R.string.player_name) + " " + SharedPreferencesManager.getString("player_name", "Enter name in Settings")

        moneyValue = findViewById(R.id.moneyValue)
        moneyValue.text = "$" + SharedPreferencesManager.getInt("player_money", 0).toString()

        var startBtn : Button = findViewById(R.id.start_btn)
        var settingsBtn : Button = findViewById(R.id.to_settings)
        var upgradeBtn : Button = findViewById(R.id.to_upgrades)
        var leaderBtn : Button = findViewById(R.id.to_leaderboard)

        startBtn.setOnClickListener {
            val intent = Intent( this, GameActivity::class.java )
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

        var adView : AdView = AdView( this )
        var adSize : AdSize = AdSize( AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT)
        adView.setAdSize( adSize )

        var adUnitId : String = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId

        var builder : AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword( "workout").addKeyword( "fitness")
        var request : AdRequest = builder.build()

        var adLayout : LinearLayout = findViewById(R.id.ad_view)
        adLayout.addView( adView )
        adView.loadAd( request )
    }

    override fun onRestart() {
        super.onRestart()
        nameValue = findViewById(R.id.nameValue)
        nameValue.text = getString(R.string.player_name) + " " + SharedPreferencesManager.getString("player_name", " ")

        moneyValue = findViewById(R.id.moneyValue)
        moneyValue.text = "$" + SharedPreferencesManager.getInt("player_money", 0).toString()
    }
}