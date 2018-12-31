package com.wl.benchmark

import com.wl.IntLottery
import org.openjdk.jmh.profile.GCProfiler
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import java.util.*


object WeightedLotteryBenchmark {
    private val N = 5000
    private val K = 50
    private val M = 500

    private val random = Random(1)
    private val randomWeights = (0 until N).map { random.nextDouble() }.toDoubleArray()
    private val powerWeights = (1..N).map { Math.pow(0.5, it.toDouble()) }.toDoubleArray()


    fun mTimesDrawKTimes(randomDistribution: Boolean, weightedLotteryF: (weights: DoubleArray) -> IntLottery) {
        (0 until M).forEach { _ -> drawKTimes(randomDistribution, weightedLotteryF) }
    }

    fun drawKTimes(randomDistribution: Boolean, weightedLotteryF: (weights: DoubleArray) -> IntLottery) {
        val weights = if (randomDistribution) randomWeights else powerWeights
        val weightedLottery = weightedLotteryF(weights)
        (0 until K).forEach { _ -> weightedLottery.draw() }
    }
}

fun main(args: Array<String>) {
    val opt = OptionsBuilder()
            .include("com.wl.benchmark.*")
            .addProfiler(GCProfiler::class.java)
            .resultFormat(ResultFormatType.JSON)
            .warmupIterations(5)
            .measurementIterations(5)
            .forks(5)
            .build()
    Runner(opt).run()
}