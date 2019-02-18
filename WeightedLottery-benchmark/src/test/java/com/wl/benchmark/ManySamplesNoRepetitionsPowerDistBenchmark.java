package com.wl.benchmark;

import com.wl.LotteryTestUtils;
import com.wl.SimpleIntWeightedLotteryNoRepetitions;
import com.wl.StatefulTwisted;
import com.wl.bingo.Bingo;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ManySamplesNoRepetitionsPowerDistBenchmark {

  private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;
  private static final double[] weights = utils.getPowerWeights();

  @Benchmark
  public void simple_05() {
    utils.mTimesDrawKTimes(() -> new SimpleIntWeightedLotteryNoRepetitions(weights, 0.5, ThreadLocalRandom::current));
  }

  @Benchmark
  public void simple_07() {
    utils.mTimesDrawKTimes(() -> new SimpleIntWeightedLotteryNoRepetitions(weights, 0.7, ThreadLocalRandom::current));
  }/*

  @Benchmark
  public void simple_09() {
    utils.mTimesDrawKTimes(() -> new SimpleIntWeightedLotteryNoRepetitions(weights, 0.9, ThreadLocalRandom::current));
  }*/

  @Benchmark
  public void bingo() {
    utils.mTimesDrawKTimes(() -> new Bingo(weights, ThreadLocalRandom.current()));
  }

  @Benchmark
  public void statefulTwisted() {
    utils.mTimesDrawKTimes(() -> new StatefulTwisted(weights, 0.7, ThreadLocalRandom::current));
  }
}
