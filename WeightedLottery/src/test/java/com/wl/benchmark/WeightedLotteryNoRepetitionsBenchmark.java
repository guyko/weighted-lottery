package com.wl.benchmark;

import com.wl.IntLottery;
import com.wl.SimpleIntWeightedLotteryNoRepetitions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 1, time = 1)
//@Measurement(iterations = 1)
//@Fork(1)
public class WeightedLotteryNoRepetitionsBenchmark {

  private static double[] weights = new double[1000];
  private static int K = 50;

  static {
    Random random = new Random(1);
    for (int i = 0; i < weights.length; ++i) {
      weights[i] = random.nextDouble();
    }
  }

  @Benchmark
  public void simple_with_threshold_05() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, weights, 0.5, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_with_threshold_06() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, weights, 0.6, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_with_threshold_07() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, weights, 0.7, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_with_threshold_08() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, weights, 0.8, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_with_threshold_09() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, weights, 0.9, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  private void draw(IntLottery weightedLottery) {
    for (int i = 0; i < K; ++i) {
      weightedLottery.draw();
    }
  }
}
