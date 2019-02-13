package com.wl

import java.util.concurrent.ThreadLocalRandom

class SimpleIntWeightedLotteryNoRepetitionsTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = SimpleIntWeightedLotteryNoRepetitions(weights = weights, random = ThreadLocalRandom::current)
}
