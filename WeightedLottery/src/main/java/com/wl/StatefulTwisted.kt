package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom

class StatefulTwisted(private val weights: DoubleArray,
                      private val random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }) : IntLottery {

  private val hitRatioThreshold: Double = 0.5
    private var indexMapping = IntArray(weights.size) { it }
    private var delegate = TwistedSister(weights, random)
    private val selected = BooleanArray(weights.size)
    private var remaining = weights.size
    private var attempts = 0
    private var hits = 0.0

    override fun draw(): Int {
        attempts++
        val item = indexMapping[delegate.draw()]
        if (!selected[item]) {
            hits++
            selected[item] = true
            remaining--
            return item
        }
        if (hits / attempts < hitRatioThreshold) {
            reallocate() // hit ratio dropped. better to reallocate the delegate

        }
        return draw()
    }

    private fun reallocate() {
        if (remaining == 0) {
            throw NoSuchElementException()
        }
        val nonSelectedWeights = DoubleArray(remaining)
        val nonSelectedIndexMapping = IntArray(remaining)
        var idx = 0
        (0 until selected.size).asSequence().filter { !selected[it] }.forEach {
            nonSelectedIndexMapping[idx] = it
            nonSelectedWeights[idx] = weights[it]
            ++idx
        }


        indexMapping = nonSelectedIndexMapping
        delegate = TwistedSister(nonSelectedWeights, random)
        hits = 0.0
        attempts = 0
    }

    override fun empty() = remaining() == 0

    override fun remaining() = remaining
}
