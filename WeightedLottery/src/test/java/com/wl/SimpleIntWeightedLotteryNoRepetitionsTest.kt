package com.wl

import java.util.*

class SimpleIntWeightedLotteryNoRepetitionsTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray, random: () -> Random) = SimpleIntWeightedLotteryNoRepetitions(weights = weights, random = random)
}