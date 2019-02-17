package com.wl

import java.util.concurrent.ThreadLocalRandom

class TwistedBrotherTest : WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = TwistedBrother(weights = weights, random = ThreadLocalRandom::current)

}
