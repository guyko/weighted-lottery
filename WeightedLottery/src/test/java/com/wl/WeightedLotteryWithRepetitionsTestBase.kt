package com.wl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class WeightedLotteryWithRepetitionsTestBase : LotteryTestBase() {

    @Test
    fun `empty weights`() {
        val weightedLottery = weightedLottery(weights = DoubleArray(0))
        assertEquals(0, weightedLottery.remaining())
        assertTrue(weightedLottery.empty())
        assertFailsWith(NoSuchElementException::class) { weightedLottery.draw() }
    }

    @Test
    fun `one element draw over and over`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(1.0))
        assertEquals(0, weightedLottery.draw())
        assertEquals(0, weightedLottery.draw())
        assertEquals(0, weightedLottery.draw())
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `100000 draws yield ~ given distribution`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.15, 0.65, 0.2))
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertInRange(15000, counters[0]!!, 500)   // binomial distribution
        assertInRange(65000, counters[1]!!, 500)
        assertInRange(20000, counters[2]!!, 500)
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `100000 draws yield ~ given distribution when some weights are 0`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65))
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertInRange(15000, counters[0]!!, 500)
        assertInRange(20000, counters[2]!!, 500)
        assertInRange(65000, counters[4]!!, 500)
        assertEquals(0, counters[1] ?: 0, "$counters doesn't match weights")
        assertEquals(0, counters[3] ?: 0, "$counters doesn't match weights")
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `100000 draws yield ~ even distribution when all weights are 0`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.0, 0.0, 0.0, 0.0))
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        (0 until 4).forEach {
            assertInRange(25000, counters[it]!!, 500)
        }

        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `invalid input result in an exception`() {
        assertFailsWith(IllegalArgumentException::class) { weightedLottery(weights = doubleArrayOf(-0.1, 0.1)) }
        assertFailsWith(IllegalArgumentException::class) { weightedLottery(weights = doubleArrayOf(Double.NaN, 0.1)) }
    }

    private fun assertInRange(expected: Int, actual: Int, grace: Int) {
        val range = (expected - grace)..(expected + grace)
        assertTrue(range.contains(actual), "$actual not in range $range")
    }
}
