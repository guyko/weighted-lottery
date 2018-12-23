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
public class WeightedLotteryNoRepetitionsBenchmark {

  private static double[] weights = new double[800];

  static {
    Random random = new Random(1);
    for (int i = 0; i < weights.length; ++i) {
      weights[i] = random.nextDouble();
    }
  }

  @Benchmark
  public void loadTestSimpleWeightedLotteryNoRepetitions05() {
    Random current = ThreadLocalRandom.current();
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(current, 5, weights, 0.5);
    for (int i = 0; i < 100; ++i) {
      weightedLottery.draw();
    }
  }

  @Benchmark
  public void loadTestSimpleWeightedLotteryNoRepetitions07() {
    Random current = ThreadLocalRandom.current();
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(current, 5, weights, 0.7);
    for (int i = 0; i < 100; ++i) {
      weightedLottery.draw();
    }
  }


  @Benchmark
  public void loadTestSimpleWeightedLotteryNoRepetitions09() {
    Random current = ThreadLocalRandom.current();
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(current, 5, weights, 0.9);
    for (int i = 0; i < 100; ++i) {
      weightedLottery.draw();
    }
  }
}
