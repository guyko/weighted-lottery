package com.wl

import java.util.concurrent.ThreadLocalRandom

class AliasLotteryNoRepetitionsTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = AliasLotteryNoRepetitions(weights = weights, random = ThreadLocalRandom::current)

}
