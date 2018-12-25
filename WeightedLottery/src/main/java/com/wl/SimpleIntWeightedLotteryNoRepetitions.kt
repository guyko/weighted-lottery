package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.NoSuchElementException

class SimpleIntWeightedLotteryNoRepetitions(private val maxAttempts: Int = 5,
                                            private val weights: DoubleArray,
                                            private val hitRatioThreshold: Double = 0.7,
                                            private val random: () -> Random = { ThreadLocalRandom.current() }) : IntLottery {

    private var indexMapping = weights.mapIndexed { k, _ -> k }.toIntArray()
    private var delegate = SimpleIntWeightedLottery(maxAttempts, weights, random)
    private var selected = 0
    private val selectedArr = weights.map { 0 }.toIntArray()
    private var attempts = 0
    private var hits = 0

    override fun draw(): Int {
        attempts++
        val item = indexMapping[delegate.draw()]
        if (selectedArr[item] == 0) {
            selected++
            hits++
            selectedArr[item] = 1
            return item
        }
        if (hits.toDouble() / attempts < hitRatioThreshold) {
            // hit ratio dropped. better to reallocate the delegate

            val nonSelectedItems = selectedArr.filter { it == 0 }.size
            if (nonSelectedItems == 0) {
                throw NoSuchElementException()
            }
            val nonSelectedWeights = DoubleArray(nonSelectedItems)
            val nonSelectedIndexMapping = IntArray(nonSelectedItems)
            var idx = 0
            (0 until selectedArr.size).filter { selectedArr[it] == 0 }.forEach {
                nonSelectedIndexMapping[idx] = it
                nonSelectedWeights[idx] = weights[it]
                ++idx
            }


            indexMapping = nonSelectedIndexMapping
            delegate = SimpleIntWeightedLottery(maxAttempts, nonSelectedWeights, random)
            hits = 0
            attempts = 0

        }
        return draw()
    }

    override fun empty() = remaining() == 0

    override fun remaining() = selectedArr.size - selected
}
