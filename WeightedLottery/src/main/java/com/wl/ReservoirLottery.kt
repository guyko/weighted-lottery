package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.NoSuchElementException

class ReservoirLottery(private val weights: DoubleArray,
                       private val k: Int,
                       private val random: () -> Random = { ThreadLocalRandom.current() }) : IntLottery {

    private val selectedToResorvoir = BooleanArray(weights.size)
    private val reservoir = IntArray(k) {
        var wsum = 0.0
        var r = -1
        var someNotSelected = -1
        (0 until weights.size).asSequence()
                .filter { !selectedToResorvoir[it] }
                .forEach {
                    someNotSelected = it
                    wsum += weights[it].validate()
                    if (random().nextDouble() < weights[it] / wsum) {
                        r = it
                    }
                }
        if (r == -1) {
            r = someNotSelected
        }
        selectedToResorvoir[r] = true
        r
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