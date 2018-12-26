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

  private static double[] randomWeights = new double[10000];
  private static double[] powerDistributionWeights = new double[10000];
  private static int K = 500;

  static {
    Random random = new Random(1);
    for (int i = 0; i < randomWeights.length; ++i) {
      randomWeights[i] = random.nextDouble();
      powerDistributionWeights[i] = i == 0 ? 0.5 : powerDistributionWeights[i] / 2;
    }
  }


  @Benchmark
  public void simple_random_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLottery(5, randomWeights, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_power_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLottery(5, powerDistributionWeights, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  private void draw(IntLottery weightedLottery) {
    for (int i = 0; i < K; ++i) {
      weightedLottery.draw();
    }
  }
}
