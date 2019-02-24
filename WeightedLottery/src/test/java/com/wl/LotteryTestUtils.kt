package com.wl

import java.util.*

object LotteryTestUtils {
    val N = 5000
    val K = 50
    val M = 50000

    private val random = Random(1)
    val randomWeights = (0 until N).map { random.nextDouble() }.toDoubleArray()
    val powerWeights = (1 until N + 1).map { Math.pow(0.9, it.toDouble()) }.shuffled().toDoubleArray()


    fun mTimesDrawKTimes(weightedLottery: () -> IntLottery) {
        (0 until M).forEach { drawKTimes(weightedLottery()) }
    }

    fun drawKTimes(weightedLottery: IntLottery) {
        (0 until K).forEach { weightedLottery.draw() }
    }
}