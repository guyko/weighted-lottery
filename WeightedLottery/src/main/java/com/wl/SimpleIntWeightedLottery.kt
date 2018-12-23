package com.wl

import mu.KotlinLogging
import java.util.*
import java.util.concurrent.ThreadLocalRandom

private val logger = KotlinLogging.logger {}

class SimpleIntWeightedLottery(private val random: Random = ThreadLocalRandom.current(),
                               private val maxAttempts: Int = 5,
                               weights: DoubleArray) : IntLottery {

    private val accumulatedWeights = run {
        var sum = 0.0
        weights.map {
            if (it < 0.0) {
                throw IllegalArgumentException("$weights contains zero weights")
            }
            sum += it
            sum
        }.toDoubleArray()
    }
    private val sumOfWeights = weights.sum()


    override fun draw(): Int {
        if (empty()) {
            logger.warn { "All weights are zero. Draw one uniformly" }
            return random.drawUniformly()
        }
        for (i in 0 until maxAttempts) {
            val key = random.nextDouble() * sumOfWeights
            val pos = Arrays.binarySearch(accumulatedWeights, key)
            if (pos >= 0) {
                //  the key was found. hitting on a boundary -- do another round. not likely to ever get here.
                continue
            }
            return -pos - 1
        }
        logger.warn { "Failed to draw by weights after $maxAttempts attempts. Drawing one uniformly. Sum of weights was: $sumOfWeights, accumulated weights were: $accumulatedWeights" }

        // draw uniformly
        return random.drawUniformly()
    }

    private fun Random.drawUniformly() = nextInt(accumulatedWeights.size)

    override fun empty() = remaining() == 0

    override fun remaining() = accumulatedWeights.size
}