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
public class OneSampleWithRepetitionsPowerDistBenchmark {

  private static WeightedLotteryBenchmark benchmark = WeightedLotteryBenchmark.INSTANCE;

  @Benchmark
  public void simple() {
    benchmark.drawKTimes(new SimpleIntWeightedLottery(5, benchmark.getPowerWeights(), ThreadLocalRandom::current));
  }
}
