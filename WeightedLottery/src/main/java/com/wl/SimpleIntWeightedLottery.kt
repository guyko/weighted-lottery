package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom

class SimpleIntWeightedLottery(private val maxAttempts: Int = 5,
                               weights: DoubleArray,
                               private val random: () -> Random = { ThreadLocalRandom.current() }) : IntLottery {
    private val accumulatedWeights = DoubleArray(weights.size)

    init {
        weights.forEachIndexed { i, w ->
            if (w.isNaN() || w < 0.0) {
                throw IllegalArgumentException("$weights contains invalid weight: $w")
            }
            accumulatedWeights[i] = w + (if (i == 0) 0.0 else accumulatedWeights[i - 1])
        }
    }


    override fun draw(): Int {
        val sumOfWeights = accumulatedWeights.last()
        if (sumOfWeights == 0.0) {
            return drawUniformly()
        }
        for (i in 0 until maxAttempts) {
            val key = random().nextDouble() * sumOfWeights
            val pos = Arrays.binarySearch(accumulatedWeights, key)
            if (pos >= 0) {
                //  the key was found. hitting on a boundary -- do another round. not likely to ever get here.
                continue
            }
            return -pos - 1
        }
        return drawUniformly()
    }

    private fun drawUniformly() = random().nextInt(accumulatedWeights.size)

    override fun empty() = remaining() == 0

    override fun remaining() = accumulatedWeights.size
}