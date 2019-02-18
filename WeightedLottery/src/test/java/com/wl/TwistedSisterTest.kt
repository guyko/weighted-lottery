package com.wl

import java.util.concurrent.ThreadLocalRandom

class TwistedSisterTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = TwistedSister(weights = weights, random = ThreadLocalRandom::current)

}
