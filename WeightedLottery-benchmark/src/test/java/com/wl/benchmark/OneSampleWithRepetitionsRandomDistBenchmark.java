package com.wl.benchmark;

import com.wl.AliasLottery;
import com.wl.LotteryTestUtils;
import com.wl.SimpleIntWeightedLottery;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class OneSampleWithRepetitionsRandomDistBenchmark {

  private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;

  @Benchmark
  public void simple() {
    utils.drawKTimes(new SimpleIntWeightedLottery(utils.getRandomWeights(), ThreadLocalRandom::current));
  }

  @Benchmark
  public void alias() {
    utils.drawKTimes(new AliasLottery(utils.getRandomWeights(), ThreadLocalRandom::current));
  }
}
