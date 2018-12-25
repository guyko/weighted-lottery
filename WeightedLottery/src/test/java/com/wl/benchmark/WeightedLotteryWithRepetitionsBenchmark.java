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

  private static double[] weights = new double[1000];
  private static int K = 50;

  static {
    Random random = new Random(1);
    for (int i = 0; i < weights.length; ++i) {
      weights[i] = random.nextDouble();
    }
  }

  @Benchmark
  public void simple() {
    IntLottery weightedLottery = new SimpleIntWeightedLottery(5, weights, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  private void draw(IntLottery weightedLottery) {
    for (int i = 0; i < K; ++i) {
      weightedLottery.draw();
    }
  }
}
