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
public class ManySamplesWithRepetitionsRandomDistBenchmark {

  private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;

  @Benchmark
  public void simple() {
    SimpleIntWeightedLottery lottery = new SimpleIntWeightedLottery(utils.getRandomWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }

  @Benchmark
  public void alias() {
    AliasLottery lottery = new AliasLottery(utils.getRandomWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }
}
