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
//@Warmup(iterations = 1, time = 1)
//@Measurement(iterations = 1)
//@Fork(1)
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
    IntLottery weightedLottery = new SimpleIntWeightedLottery(5, weights, ThreadLocalRandom::current);
    for (int i = 0; i < 100; ++i) {
      weightedLottery.draw();
    }
  }
}
