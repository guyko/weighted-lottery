package com.wl

import org.junit.Test
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.NoSuchElementException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

abstract class WeightedLotteryNoRepetitionsTestBase {

    abstract fun weightedLottery(random: Random, weights: DoubleArray): IntLottery

    @Test
    fun `one draw for one element`() {
        val weightedLottery = weightedLottery(random = ThreadLocalRandom.current(), weights = arrayOf(1.0).toDoubleArray())
        assertEquals(0, weightedLottery.draw())
        assertEquals(0, weightedLottery.remaining())
        assertNoMoreDraws(weightedLottery)
    }

    @Test
    fun `one draw for each element`() {
        val weightedLottery = weightedLottery(random = ThreadLocalRandom.current(), weights = arrayOf(0.2, 0.5, 1.0, 1.0, 0.75).toDoubleArray())
        val elements = (0 until 5).map { weightedLottery.draw() }.toSet()
        assertEquals((0 until 5).toSet(), elements)
        assertEquals(0, weightedLottery.remaining())
        assertNoMoreDraws(weightedLottery)
    }

    @Test
    fun `one draw for each element, with zero weights`() {
        val weightedLottery = weightedLottery(random = ThreadLocalRandom.current(), weights = arrayOf(0.0, 0.5, 1.0, 0.0, 0.75).toDoubleArray())
        val elements = (0 until 5).map { weightedLottery.draw() }.toSet()
        assertEquals((0 until 5).toSet(), elements)
        assertEquals(0, weightedLottery.remaining())
        assertNoMoreDraws(weightedLottery)
    }


    @Test
    fun `100000 draws yield ~ given distribution`() {
        val random = Random(1)
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery(random = random, weights = arrayOf(0.15, 0.65, 0.2).toDoubleArray()).draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertInRange(15000, counters[0]!!, 200)
        assertInRange(65000, counters[1]!!, 200)
        assertInRange(20000, counters[2]!!, 200)
    }

    @Test
    fun `100000 draws yield ~ given distribution when some weights are 0`() {
        val random = Random(1)
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery(random = random, weights = arrayOf(0.15, 0.0, 0.2, 0.0, 0.65).toDoubleArray()).draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertInRange(15000, counters[0]!!, 200)
        assertInRange(20000, counters[2]!!, 200)
        assertInRange(65000, counters[4]!!, 200)
        assertEquals(0, counters[1] ?: 0, "$counters doesn't match weights")
        assertEquals(0, counters[3] ?: 0, "$counters doesn't match weights")
    }

    private fun assertInRange(expected: Int, actual: Int, grace: Int) {
        val range = (expected - grace)..(expected + grace)
        assertTrue(range.contains(actual), "$actual not in range $range")
    }

    private fun assertNoMoreDraws(weightedLottery: IntLottery) {
        assertTrue(weightedLottery.empty())
        assertFailsWith(NoSuchElementException::class) { weightedLottery.draw() }
    }
}