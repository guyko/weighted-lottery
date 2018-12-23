package com.wl.benchmark;

import com.wl.IntLottery;
import com.wl.SimpleIntWeightedLottery;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class WeightedLotteryWithRepetitionsBenchmark {

  private static double[] weights = new double[800];

  static {
    Random random = new Random(1);
    for (int i = 0; i < weights.length; ++i) {
      weights[i] = random.nextDouble();
    }
  }

  @Benchmark
  public void loadTestSimpleWeightedLottery() {
    Random current = ThreadLocalRandom.current();
    IntLottery weightedLottery = new SimpleIntWeightedLottery(current, 5, weights);
    for (int i = 0; i < 100; ++i) {
      weightedLottery.draw();
    }
  }
}
