package com.wl

import java.util.*

class SimpleIntWeightedLotteryTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray, random: () -> Random) = SimpleIntWeightedLottery(weights = weights, random = random)
}