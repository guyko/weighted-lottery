package com.wl

import java.util.concurrent.ThreadLocalRandom

class AliasLotteryTest: WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray, random: () -> ThreadLocalRandom) = AliasLottery(weights = weights, random = random)

}
