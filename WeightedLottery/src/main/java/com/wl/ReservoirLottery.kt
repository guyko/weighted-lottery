package com.wl

import java.util.concurrent.ThreadLocalRandom

class ReservoirLottery(private val weights: DoubleArray,
                       private val k: Int,
                       private val random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }) : IntLottery {

    private val reservoir: IntArray

    init {
        val firstKLottery = SimpleIntWeightedLotteryNoRepetitions(weights = weights.sliceArray(0 until k), random = random)
        var wSum = 0.0
        reservoir = IntArray(k) {
            wSum += (weights[it].validate() / k)
            firstKLottery.draw()
        }

        (k + 1 until weights.size).forEach {
            wSum += (weights[it].validate() / k)
            val p = weights[it] / wSum
            val j = random().nextDouble()
            if (j <= p) {
                reservoir[random().nextInt(k)] = it
            }
        }
    }

    private var cursor = 0

    override fun draw(): Int {
        if (empty()) {
            throw NoSuchElementException()
        }
        val next = reservoir[cursor]
        cursor++
        return next
    }

    override fun empty() = remaining() == 0

    override fun remaining() = k - cursor

    private fun Double.validate(): Double {
        if (isNaN() || this < 0.0) {
            throw IllegalArgumentException("$weights contains invalid weight: $this")
        }
        return this
    }
}