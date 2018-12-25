package com.wl

import mu.KotlinLogging
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.NoSuchElementException

private val logger = KotlinLogging.logger {}

class SimpleIntWeightedLottery(private val maxAttempts: Int = 5,
                               weights: DoubleArray,
                               private val random: () -> Random = { ThreadLocalRandom.current() }) : IntLottery {

    private val accumulatedWeights = run {
        var sum = 0.0
        weights.map {
            if (it.isNaN() || it < 0.0) {
                throw IllegalArgumentException("$weights contains invalid weight: $it")
            }
            sum += it
            sum
        }.toDoubleArray()
    }


    override fun draw(): Int {
        if (accumulatedWeights.isEmpty()) {
            throw NoSuchElementException()
        }
        val sumOfWeights = sumOfWeights()
        if (sumOfWeights == 0.0) {
            logger.warn { "All weights are zero. Draw one uniformly" }
            return random().drawUniformly()
        }
        for (i in 0 until maxAttempts) {
            val key = random().nextDouble() * sumOfWeights()
            val pos = Arrays.binarySearch(accumulatedWeights, key)
            if (pos >= 0) {
                //  the key was found. hitting on a boundary -- do another round. not likely to ever get here.
                continue
            }
            return -pos - 1
        }
        logger.warn { "Failed to draw by weights after $maxAttempts attempts. Drawing one uniformly. Sum of weights was: ${sumOfWeights()}, accumulated weights were: $accumulatedWeights" }

        // draw uniformly
        return random().drawUniformly()
    }

    private fun sumOfWeights() = accumulatedWeights.last()

    private fun Random.drawUniformly() = nextInt(accumulatedWeights.size)

    override fun empty() = remaining() == 0

    override fun remaining() = accumulatedWeights.size
}