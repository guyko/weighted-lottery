package com.wl

import com.wl.benchmark.WeightedLotteryBenchmark
import org.junit.Test
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.assertFalse

abstract class LotteryTestBase {
    abstract fun weightedLottery(weights: DoubleArray, random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }): IntLottery

    @Test
    fun `one sample works with normal distribution`() {
        val weightedLottery = weightedLottery(weights = WeightedLotteryBenchmark.randomWeights)
        WeightedLotteryBenchmark.drawKTimes(weightedLottery)
        assertFalse { weightedLottery.empty() }
    }

    @Test
    fun `one sample works with exponential distribution`() {
        val weightedLottery = weightedLottery(weights = WeightedLotteryBenchmark.powerWeights)
        assertFalse { weightedLottery.empty() }
    }
}