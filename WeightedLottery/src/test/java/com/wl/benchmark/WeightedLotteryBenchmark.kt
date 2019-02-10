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
    val randomWeights = (0 until N).map { random.nextDouble() }.toDoubleArray()
    val powerWeights = (0 until N).map { Math.pow(0.5, it.toDouble()) }.shuffled().toDoubleArray()


    fun mTimesDrawKTimes(weightedLottery: () -> IntLottery) {
        (0 until M).forEach { drawKTimes(weightedLottery()) }
    }

    fun drawKTimes(weightedLottery: IntLottery) {
        (0 until K).forEach { weightedLottery.draw() }
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