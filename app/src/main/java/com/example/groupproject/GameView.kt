package com.example.groupproject

import android.content.Context
import android.graphics.*
import android.view.View

class GameView(
    context: Context,
    private val width: Int,
    private val height: Int,
    private val towerMaxHealth: Int,
    private val towerDefenseMaxHealth: Int,
    private val enemySpeed: Float
) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 5.0f
        style = Paint.Style.STROKE
    }

    private val towerBitmap = BitmapFactory.decodeResource(resources, R.drawable.tower)
    private val game = Game(300, enemySpeed, width, height, towerMaxHealth, towerDefenseMaxHealth)

    init {
        val towerCenter = Point(width / 2, height / 2)
        val gameBounds = Rect(0, 0, width, height)
        game.setTower(towerCenter, gameBounds)
    }

    fun getGame(): Game = game

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw tower
        val towerCenter = game.getTowerCenter()
        val radius = game.getTowerRadius()

        if (game.getIsDefenseAlive()) {
            paint.color = Color.RED // Red for defense circle
            canvas.drawCircle(towerCenter.x.toFloat(), towerCenter.y.toFloat(), radius.toFloat(), paint)
        }

        canvas.drawBitmap(towerBitmap, null, Rect(towerCenter.x - 300, towerCenter.y - 400, towerCenter.x + 400, towerCenter.y + 400), paint)
        // Draw Enemies
        for (enemy in game.getEnemies()) {
            paint.color = Color.RED
            canvas.drawCircle(enemy.enemyPoint.x.toFloat(), enemy.enemyPoint.y.toFloat(), 10.0f, paint)
        }
    }

    companion object {
        const val DELTA_TIME = 100
    }
}