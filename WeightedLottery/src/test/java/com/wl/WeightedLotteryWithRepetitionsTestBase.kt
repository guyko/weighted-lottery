package com.wl

import org.junit.Test
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class WeightedLotteryWithRepetitionsTestBase {

    abstract fun weightedLottery(random: Random, weights: DoubleArray): IntLottery

    @Test
    fun testOneElement() {
        val weightedLottery = weightedLottery(random = ThreadLocalRandom.current(), weights = arrayOf(1.0).toDoubleArray())
        assertEquals(0, weightedLottery.draw())
        assertEquals(0, weightedLottery.draw())
        assertEquals(0, weightedLottery.draw())
        assertEquals(1, weightedLottery.remaining())
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun test100000Times() {
        val weightedLottery = weightedLottery(random = Random(1), weights = arrayOf(0.15, 0.2, 0.65).toDoubleArray())
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        assertTrue(counters[0]!! < 15200, "$counters doesn't match weights")
        assertTrue(counters[0]!! > 14800, "$counters doesn't match weights")
        assertTrue(counters[1]!! < 20200, "$counters doesn't match weights")
        assertTrue(counters[1]!! > 19800, "$counters doesn't match weights")
        assertTrue(counters[2]!! < 65200, "$counters doesn't match weights")
        assertTrue(counters[2]!! > 64800, "$counters doesn't match weights")
        assertEquals(3, weightedLottery.remaining())
        assertFalse(weightedLottery.empty())
    }

    @Test
    fun test100000TimesWithAllZeros() {
        val weightedLottery = weightedLottery(random = Random(1), weights = arrayOf(0.0, 0.0, 0.0, 0.0).toDoubleArray())
        val counters = mutableMapOf<Int, Int>()
        (0 until 100000).forEach { _ ->
            val idx = weightedLottery.draw()
            counters[idx] = (counters[idx] ?: 0) + 1
        }
        (0 until 4).forEach {
            assertTrue(counters[it]!! < 25500, "$counters doesn't match weights")
            assertTrue(counters[it]!! > 24500, "$counters doesn't match weights")
        }

        assertEquals(4, weightedLottery.remaining())
        assertFalse(weightedLottery.empty())
    }
}