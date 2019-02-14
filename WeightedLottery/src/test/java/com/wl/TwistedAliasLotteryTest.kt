package com.wl

import java.util.concurrent.ThreadLocalRandom

class TwistedAliasLotteryTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = TwistedAliasLottery(weights = weights, random = ThreadLocalRandom::current)

}
