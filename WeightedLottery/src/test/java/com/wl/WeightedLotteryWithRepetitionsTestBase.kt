package com.wl

import com.wl.benchmark.WeightedLotteryBenchmark
import org.junit.Test
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class WeightedLotteryWithRepetitionsTestBase {

    abstract fun weightedLottery(weights: DoubleArray, random: () -> Random = { ThreadLocalRandom.current() }): IntLottery

    @Test
    fun `init works with normal distribution`() {
        val weightedLottery = weightedLottery(weights = WeightedLotteryBenchmark.randomWeights)
        assertTrue(weightedLottery.draw() >= 0)
    }

    @Test
    fun `init works with exponential distribution`() {
        val weightedLottery = weightedLottery(weights = WeightedLotteryBenchmark.powerWeights)
        assertTrue(weightedLottery.draw() >= 0)
    }

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
        val random = Random(1)
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.15, 0.65, 0.2)) { random }
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertInRange(15000, counters[0]!!, 200)   // binomial distribution
        assertInRange(65000, counters[1]!!, 200)
        assertInRange(20000, counters[2]!!, 200)
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `100000 draws yield ~ given distribution when some weights are 0`() {
        val random = Random(1)
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65)) { random }
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertInRange(15000, counters[0]!!, 200)
        assertInRange(20000, counters[2]!!, 200)
        assertInRange(65000, counters[4]!!, 200)
        assertEquals(0, counters[1] ?: 0, "$counters doesn't match weights")
        assertEquals(0, counters[3] ?: 0, "$counters doesn't match weights")
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `100000 draws yield ~ even distribution when all weights are 0`() {
        val random = Random(1)
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.0, 0.0, 0.0, 0.0)) { random }
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        (0 until 4).forEach {
            assertInRange(25000, counters[it]!!, 250)
        }
        
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun `invalid input result in an exception`() {
        assertFailsWith(IllegalArgumentException::class) { weightedLottery(weights = doubleArrayOf(-0.1, 0.1)) { Random(1) } }
        assertFailsWith(IllegalArgumentException::class) { weightedLottery(weights = doubleArrayOf(Double.NaN, 0.1)) { Random(1) } }
    }

    private fun assertInRange(expected: Int, actual: Int, grace: Int) {
        val range = (expected - grace)..(expected + grace)
        assertTrue(range.contains(actual), "$actual not in range $range")
    }
}