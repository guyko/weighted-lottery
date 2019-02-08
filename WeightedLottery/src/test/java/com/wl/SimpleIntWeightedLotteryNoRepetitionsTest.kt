package com.wl

import java.util.concurrent.ThreadLocalRandom

class SimpleIntWeightedLotteryNoRepetitionsTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray, random: () -> ThreadLocalRandom) = SimpleIntWeightedLotteryNoRepetitions(weights = weights, random = random)
}
