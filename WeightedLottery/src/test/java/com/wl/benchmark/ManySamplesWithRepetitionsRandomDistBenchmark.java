package com.wl.benchmark;

import com.wl.SimpleIntWeightedLottery;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ManySamplesWithRepetitionsRandomDistBenchmark {

  @Benchmark
  public void simple() {
    WeightedLotteryBenchmark.INSTANCE.mTimesDrawKTimes(true, weights -> new SimpleIntWeightedLottery(5, weights, ThreadLocalRandom::current));
  }
}
