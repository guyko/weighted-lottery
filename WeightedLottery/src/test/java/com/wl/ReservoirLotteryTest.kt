package com.wl

import java.util.concurrent.ThreadLocalRandom

class ReservoirLotteryTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = ReservoirLottery(weights, weights.size, ThreadLocalRandom::current)
}