package com.wl

import java.util.concurrent.ThreadLocalRandom

class SumTreeLotteryTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = SumTreeLottery(weights, ThreadLocalRandom.current())
}
