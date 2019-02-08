package com.wl

import java.util.concurrent.ThreadLocalRandom

class SimpleIntWeightedLotteryTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray, random: () -> ThreadLocalRandom) = SimpleIntWeightedLottery(weights = weights, random = random)
}
