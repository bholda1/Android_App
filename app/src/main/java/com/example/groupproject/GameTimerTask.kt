package com.example.groupproject

import java.util.TimerTask

class GameTimerTask : TimerTask {
    private lateinit var activity : GameActivity

    constructor( activity: GameActivity ) {
        this.activity = activity
    }

    override fun run() {
        activity.updateModel( )
        activity.updateView( )
    }
}