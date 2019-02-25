package com.wl.benchmark;

import com.wl.LotteryTestUtils;
import com.wl.ReservoirLottery;
import com.wl.SimpleIntWeightedLotteryNoRepetitions;
import com.wl.StatefulTwisted;
import com.wl.SumTreeLottery;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ManySamplesNoRepetitionsRandomDistBenchmark {

  private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;
  private static final double[] weights = utils.getRandomWeights();

  @Benchmark
  public void simple_05() {
    utils.mTimesDrawKTimes(() -> new SimpleIntWeightedLotteryNoRepetitions(weights, 0.5, ThreadLocalRandom::current));
  }

  @Benchmark
  public void simple_07() {
    utils.mTimesDrawKTimes(() -> new SimpleIntWeightedLotteryNoRepetitions(weights, 0.7, ThreadLocalRandom::current));
  }

  @Benchmark
  public void reservoir() {
    utils.mTimesDrawKTimes(() -> new ReservoirLottery(weights, LotteryTestUtils.INSTANCE.getK(), ThreadLocalRandom::current));
  }

  @Benchmark
  public void sumTree() {
    utils.mTimesDrawKTimes(() -> new SumTreeLottery(weights, ThreadLocalRandom.current()));
  }

  @Benchmark
  public void statefulTwisted() {
    utils.mTimesDrawKTimes(() -> new StatefulTwisted(weights, ThreadLocalRandom::current));
  }
}
