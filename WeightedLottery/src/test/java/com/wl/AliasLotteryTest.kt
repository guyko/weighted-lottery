package com.wl

import java.util.concurrent.ThreadLocalRandom

class AliasLotteryTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = AliasLottery(weights = weights, random = ThreadLocalRandom::current)

}
