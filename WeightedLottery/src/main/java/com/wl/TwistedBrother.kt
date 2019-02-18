package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicBoolean

private const val ONE = 1.000000
private const val ZERO = 0.0


class TwistedBrother(
    private val weights: DoubleArray,
    private val random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }
) : IntLottery {
    private val size = weights.size
    private lateinit var probabilities: DoubleArray
    private lateinit var aliases : IntArray
    private var slow = true
    private val slowLottery = MySimpleIntWeightedLottery(weights, random)
    private var counter = 0
    private val notInited = AtomicBoolean(true)
    private val whenToInit = 10_000


    private fun initSlow() {
        probabilities = DoubleArray(size) { ONE }
        aliases = IntArray(size) { random().nextInt(size) }
        var sumOfWeights = 0.0
        for (index in 0 until size) {
            sumOfWeights += weights[index]
        }
        val bigger = LinkedList<Int>()
        val smaller = LinkedList<Int>()
        val values = DoubleArray(size)
        for (index in 0 until size) {
            val value  = weights[index].validate() * size / sumOfWeights
            putItemInCorrectBag(index, value, bigger, smaller)
            values[index] = value
        }
        while (smaller.isNotEmpty() && bigger.isNotEmpty()) {
            val pickSmaller = smaller.pop()
            val pickSmallerValue = values[pickSmaller]
            val pickBigger = bigger.pop()
            val pickBiggerValue = values[pickBigger]
            val delta = pickBiggerValue + pickSmallerValue - ONE
            probabilities[pickSmaller] = pickSmallerValue
            aliases[pickSmaller] = pickBigger
            putItemInCorrectBag(pickBigger, delta, bigger, smaller)
        }
        smaller.forEach { probabilities[it] = ONE }
        bigger.forEach { probabilities[it] = ONE }
        slow = false
    }

    private inline fun putItemInCorrectBag(
        index: Int,
        value: Double,
        bigger: MutableList<Int>,
        smaller: MutableList<Int>
    ) {

        when {
            value.compareTo(ONE) > 0 -> bigger.add(index)
            value.compareTo(ONE) < 0 -> smaller.add(index)
            else -> probabilities[index] = ONE
        }
    }

    override fun draw(): Int {
        if (slow) {
            if (counter > whenToInit) {
                if (notInited.compareAndSet(true, false)) {
                    Thread() { initSlow() }.start()
                }
            }
            counter ++
            return slowLottery.draw()
        }
        if (weights.isEmpty()) {
            throw NoSuchElementException()
        }
        val index = random().nextInt(weights.size)
        val prob = random().nextDouble()
        return if (prob < probabilities[index]) {
            index
        } else {
            aliases[index]
        }
    }

    override fun empty(): Boolean {
        return weights.isEmpty()
    }

    override fun remaining(): Int {
        return weights.size
    }

    private fun Double.validate(): Double {
        if (isNaN() || this < ZERO) {
            throw IllegalArgumentException("$weights contains invalid weight: $this")
        }
        return this
    }

    private class MySimpleIntWeightedLottery(private val weights: DoubleArray,
                                             private val random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }) : IntLottery {
        private val accumulatedWeights = DoubleArray(weights.size)

        init {
            if (weights.isNotEmpty()) {
                accumulatedWeights[0] = weights[0].validate()
                for (index in 1 until weights.size) {
                    accumulatedWeights[index] = weights[index].validate() + accumulatedWeights[index - 1]
                }
            }
        }


        override fun draw(): Int {
            val sumOfWeights = accumulatedWeights.last()
            if (sumOfWeights == 0.0) {
                return drawUniformly()
            }
            val key = random().nextDouble() * sumOfWeights
            val pos = Arrays.binarySearch(accumulatedWeights, key)
            return when {
                pos < 0 -> -pos - 1
                else -> pos
            }
        }

        private fun Double.validate(): Double {
            if (isNaN() || this < 0.0) {
                throw IllegalArgumentException("$weights contains invalid weight: $this")
            }
            return this
        }

        private fun drawUniformly() = random().nextInt(accumulatedWeights.size)

        override fun empty() = remaining() == 0

        override fun remaining() = accumulatedWeights.size
    }
}

