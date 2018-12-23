package com.wl

import java.util.*

class SimpleIntWeightedLotteryTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(random: Random, weights: DoubleArray) = SimpleIntWeightedLottery(random = random, weights = weights)
}