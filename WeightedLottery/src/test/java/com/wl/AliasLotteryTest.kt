package com.wl

import org.junit.Assert.*
import java.util.*

class AliasLotteryTest: WeightedLotteryWithRepetitionsTestBase() {
    override fun weightedLottery(weights: DoubleArray, random: () -> Random) = AliasLottery(weights = weights, random = random)

}