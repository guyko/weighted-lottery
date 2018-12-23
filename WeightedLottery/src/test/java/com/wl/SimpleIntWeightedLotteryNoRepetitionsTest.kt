package com.wl

import java.util.*

class SimpleIntWeightedLotteryNoRepetitionsTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(random: Random, weights: DoubleArray) = SimpleIntWeightedLotteryNoRepetitions(random = random, weights = weights)
}