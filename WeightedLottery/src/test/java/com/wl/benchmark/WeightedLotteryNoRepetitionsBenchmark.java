package com.wl.benchmark;

import com.wl.IntLottery;
import com.wl.SimpleIntWeightedLotteryNoRepetitions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 1, time = 1)
//@Measurement(iterations = 1)
//@Fork(1)
public class WeightedLotteryNoRepetitionsBenchmark {

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
  public void simple_05_random_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, randomWeights, 0.5, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_07_random_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, randomWeights, 0.7, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_09_random_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, randomWeights, 0.9, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_05_power_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, powerDistributionWeights, 0.5, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_07_power_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, powerDistributionWeights, 0.7, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  @Benchmark
  public void simple_09_power_dist() {
    IntLottery weightedLottery = new SimpleIntWeightedLotteryNoRepetitions(5, powerDistributionWeights, 0.9, ThreadLocalRandom::current);
    draw(weightedLottery);
  }

  private void draw(IntLottery weightedLottery) {
    for (int i = 0; i < K; ++i) {
      weightedLottery.draw();
    }
  }

  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
            .include("com.wl.benchmark.*")
            .addProfiler(GCProfiler.class)
            .resultFormat(ResultFormatType.JSON)
            .warmupIterations(5)
            .measurementIterations(5)
            .forks(5)
            .build();

    new Runner(opt).run();
  }
}
