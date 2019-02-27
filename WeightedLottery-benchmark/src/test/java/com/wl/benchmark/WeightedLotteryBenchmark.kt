package com.wl.benchmark

import org.openjdk.jmh.profile.GCProfiler
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder


fun main(args: Array<String>) {
    val opt = OptionsBuilder()
            .include("com.wl.benchmark.*")
            .addProfiler(GCProfiler::class.java)
            .resultFormat(ResultFormatType.JSON)
            .warmupIterations(3)
            .measurementIterations(3)
            .forks(3)
            .build()
    Runner(opt).run()
}