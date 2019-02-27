package com.wl.benchmark;

import com.wl.IntLottery;
import com.wl.LotteryTestUtils;
import com.wl.ReservoirLottery;
import com.wl.SimpleIntWeightedLotteryNoRepetitions;
import com.wl.StatefulTwisted;
import com.wl.SumTreeLottery;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;

public class LotteryNoRepetitionsBenchmark {


  @State(Scope.Thread)
  public static class Simple05State extends LotteryState {
    @NotNull
    IntLottery newLottery(double[] weights) {
      return new SimpleIntWeightedLotteryNoRepetitions(weights, 0.5, ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class Simple07State extends LotteryState {

    @NotNull
    IntLottery newLottery(double[] weights) {
      return new SimpleIntWeightedLotteryNoRepetitions(weights, 0.7, ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class ReservoirState extends LotteryState {
    @Override
    IntLottery newLottery(double[] weights) {
      return new ReservoirLottery(weights, LotteryTestUtils.INSTANCE.getK(), ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class SumTreeState extends LotteryState {
    @Override
    IntLottery newLottery(double[] weights) {
      return new SumTreeLottery(weights, ThreadLocalRandom.current());
    }
  }

  @State(Scope.Thread)
  public static class StatefulTwistedState extends LotteryState {
    @Override
    IntLottery newLottery(double[] weights) {
      return new StatefulTwisted(weights, ThreadLocalRandom::current);
    }
  }


  @Benchmark
  public int simple_05(Simple05State state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int simple_07(Simple07State state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int reservoir(ReservoirState state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int sumTree(SumTreeState state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int statefulTwisted(StatefulTwistedState state) {
    return benchmarkImpl(state);
  }


  private int benchmarkImpl(LotteryState state) {
    IntLottery lottery = state.getLottery();
    if (state.benchmarkType != EBenchmarkType.Init) {
      LotteryTestUtils.INSTANCE.drawKTimes(lottery);
    }
    return lottery.remaining();
  }
}
