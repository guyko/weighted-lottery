package com.wl

import java.util.concurrent.ThreadLocalRandom

class StatefulTwistedTest : WeightedLotteryNoRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray) = StatefulTwisted(weights = weights, random = ThreadLocalRandom::current)
}
