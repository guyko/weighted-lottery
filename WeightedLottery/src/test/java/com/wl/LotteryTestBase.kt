package com.wl

import org.junit.Test
import kotlin.test.assertFalse

abstract class LotteryTestBase {

    abstract fun weightedLottery(weights: DoubleArray): IntLottery

    @Test
    fun `one sample works with normal distribution`() {
        val weightedLottery = weightedLottery(weights = LotteryTestUtils.randomWeights)
        LotteryTestUtils.drawKTimes(weightedLottery)
        assertFalse { weightedLottery.empty() }
    }

    @Test
    fun `one sample works with exponential distribution`() {
        val weightedLottery = weightedLottery(weights = LotteryTestUtils.powerWeights)
        LotteryTestUtils.drawKTimes(weightedLottery)
        assertFalse { weightedLottery.empty() }
    }
}