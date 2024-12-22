package com.example.groupproject

import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import kotlin.math.sqrt
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Game (
    private val towerRadius: Int,
    private var enemySpeed: Float,
    private val screenWidth: Int,
    private val screenHeight: Int,
    private var towerMaxHealth: Int = 5,
    private var towerDefenseMaxHealth: Int = 1
) {
    private val enemies = mutableListOf<Enemy>()
    private var towerCenter: Point = Point()
    private var gameBounds: Rect = Rect()
    private var currentWave = 0
    private var isGameRunning = true

    private var numTowerHits : Int = 0
    private var numDefenseHits : Int = 0

    private var isDefenseAlive : Boolean = true

    ////// TOWER STATS (for upgrades) //////
    private var money : Int = 0

    fun setTower(center: Point, bounds: Rect) {
        towerCenter = center
        gameBounds = bounds
    }

    fun spawnEnemies(numEnemies: Int) {
        enemies.clear()

        repeat(numEnemies) {
            val side = (1..4).random()
            val position = when (side) {
                1 -> Point((0..screenWidth).random(), 0)
                2 -> Point(screenWidth, (0..screenHeight).random())
                3 -> Point((0..screenWidth).random(), screenHeight)
                4 -> Point(0, (0..screenHeight).random())
                else -> Point(0, 0)
            }
            enemies.add(Enemy(position, side))
        }
    }

    fun startWave() {
        val enemyCount = (5 + currentWave * 1.5).toInt().coerceAtMost(30)
        Log.w("MA", "number of enemies spawned: " + enemyCount)

        spawnEnemies(enemyCount)
        currentWave++
    }

    fun moveEnemiesTowardsTower() {
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            val directionX = towerCenter.x - enemy.enemyPoint.x
            val directionY = towerCenter.y - enemy.enemyPoint.y
            val magnitude = sqrt((directionX * directionX + directionY * directionY).toDouble()).toFloat()

            if (magnitude > 0.01f) {
                val angle = atan2((towerCenter.y - enemy.enemyPoint.y).toDouble(), (towerCenter.x - enemy.enemyPoint.x).toDouble())

                enemy.enemyPoint.x += (enemySpeed * GameView.DELTA_TIME * cos(angle)).toInt()
                enemy.enemyPoint.y += (enemySpeed * GameView.DELTA_TIME * sin(angle)).toInt()
            }

            if (magnitude <= towerRadius) {
                if (isDefenseAlive) {
                    // Damage the defense
                    numDefenseHits++
                    // Increase money
                    money += 1
                    Log.w("MA", "collision with ring")

                    if (numDefenseHits >= towerDefenseMaxHealth) {
                        isDefenseAlive = false
                    }
                    iterator.remove() // Remove enemy after hitting defense
                } else {
                    // Damage the tower
                    numTowerHits++
                    money += 1
                    Log.w("MA", "collision with tower")

                    iterator.remove()
                    if (numTowerHits > towerMaxHealth) {
                        isGameRunning = false
                    }
                }
            }
        }
    }

    fun getEnemies() = enemies
    fun getTowerCenter() = towerCenter
    fun getTowerRadius() = towerRadius
    fun isRunning() = isGameRunning
    fun getMoney() = money
    fun getWave() = currentWave
    fun getIsDefenseAlive() = isDefenseAlive
    fun getTowerMaxHealth(): Int = towerMaxHealth
    fun getDefenseMaxHealth(): Int = towerDefenseMaxHealth
    fun getTowerHealth() = towerMaxHealth - numTowerHits
    fun getDefenseHealth() : Int {
        return if (isDefenseAlive) towerDefenseMaxHealth - numDefenseHits else 0
    }

    data class Enemy(
        var enemyPoint : Point,
        var side : Int
    )
}