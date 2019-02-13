package com.wl

import java.util.concurrent.ThreadLocalRandom

class SimpleIntWeightedLotteryTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = SimpleIntWeightedLottery(weights = weights, random = ThreadLocalRandom::current)
}
