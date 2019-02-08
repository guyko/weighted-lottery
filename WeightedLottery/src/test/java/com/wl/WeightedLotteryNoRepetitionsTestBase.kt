package com.wl

import org.junit.Test
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

abstract class WeightedLotteryNoRepetitionsTestBase {

    abstract fun weightedLottery(weights: DoubleArray, random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }): IntLottery

    @Test
    fun `empty weights`() {
        val weightedLottery = weightedLottery(weights = DoubleArray(0))
        assertNoMoreDraws(weightedLottery)
    }

    @Test
    fun `one draw for one element`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(1.0))
        assertEquals(0, weightedLottery.draw())
        assertNoMoreDraws(weightedLottery)
    }

    @Test
    fun `one draw for each element`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.2, 0.5, 1.0, 1.0, 0.75))
        val elements = (0 until 5).map { weightedLottery.draw() }.toSet()
        assertEquals((0 until 5).toSet(), elements)
        assertNoMoreDraws(weightedLottery)
    }

    @Test
    fun `one draw for each element with large ratios`() {
        val weights = doubleArrayOf(1E+100, 1.0, 1E-100)
        val weightedLottery = weightedLottery(weights = weights)
        (0 until weights.size).forEach { assertEquals(it, weightedLottery.draw()) }
        assertNoMoreDraws(weightedLottery)
    }

    @Test
    fun `one draw for each element, with zero weights`() {
        val weightedLottery = weightedLottery(weights = doubleArrayOf(0.0, 0.5, 1.0, 0.0, 0.75))
        val elements = (0 until 5).map { weightedLottery.draw() }.toSet()
        assertEquals((0 until 5).toSet(), elements)
        assertNoMoreDraws(weightedLottery)
    }


    @Test
    fun `100000 draws yield ~ given distribution`() {
        val counters = mutableMapOf<Int, Int>()
        val countersWhenFirstWas0_65 = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val weightedLottery = weightedLottery(weights = doubleArrayOf(0.15, 0.65, 0.2))
            val i0 = weightedLottery.draw()
            counters[i0] = (counters[i0] ?: 0) + 1
            if (i0 == 1) {
                val i1 = weightedLottery.draw()
                countersWhenFirstWas0_65[i1] = (countersWhenFirstWas0_65[i1] ?: 0) + 1
            }
        }
        assertInRange(15000, counters[0]!!, 500)
        assertInRange(65000, counters[1]!!, 500)
        assertInRange(20000, counters[2]!!, 500)


        assertInRange((counters[1]!! * (0.15 / 0.35)).toInt(), countersWhenFirstWas0_65[0]!!, 500)
        assertInRange((counters[1]!! * (0.2 / 0.35)).toInt(), countersWhenFirstWas0_65[2]!!, 500)
    }

    @Test
    fun `100000 draws yield ~ given distribution when some weights are 0`() {
        val counters = mutableMapOf<Int, Int>()
        val countersWhenFirstWas0_2 = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val weightedLottery = weightedLottery(weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65))
            val i0 = weightedLottery.draw()
            counters[i0] = (counters[i0] ?: 0) + 1
            if (i0 == 2) {
                val i1 = weightedLottery.draw()
                countersWhenFirstWas0_2[i1] = (countersWhenFirstWas0_2[i1] ?: 0) + 1
            }
        }
        assertInRange(15000, counters[0]!!, 500)
        assertInRange(20000, counters[2]!!, 500)
        assertInRange(65000, counters[4]!!, 500)
        assertEquals(0, counters[1] ?: 0, "$counters doesn't match weights")
        assertEquals(0, counters[3] ?: 0, "$counters doesn't match weights")

        assertInRange((counters[2]!! * (0.15 / 0.8)).toInt(), countersWhenFirstWas0_2[0]!!, 500)
        assertInRange((counters[2]!! * (0.65 / 0.8)).toInt(), countersWhenFirstWas0_2[4]!!, 500)
    }

    @Test
    fun `invalid input result in an exception`() {
        assertFailsWith(IllegalArgumentException::class) { weightedLottery(weights = doubleArrayOf(-0.1, 0.1))}
        assertFailsWith(IllegalArgumentException::class) { weightedLottery(weights = doubleArrayOf(Double.NaN, 0.1))}
    }

    private fun assertInRange(expected: Int, actual: Int, grace: Int) {
        val range = (expected - grace)..(expected + grace)
        assertTrue(range.contains(actual), "$actual not in range $range")
    }

    private fun assertNoMoreDraws(weightedLottery: IntLottery) {
        assertEquals(0, weightedLottery.remaining())
        assertTrue(weightedLottery.empty())
        assertFailsWith(NoSuchElementException::class) { weightedLottery.draw() }
    }
}
