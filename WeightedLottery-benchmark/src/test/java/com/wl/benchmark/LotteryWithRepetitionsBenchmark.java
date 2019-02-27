package com.wl.benchmark;

import com.wl.AliasLottery;
import com.wl.IntLottery;
import com.wl.LotteryTestUtils;
import com.wl.SimpleIntWeightedLottery;
import com.wl.TwistedAliasLottery;
import com.wl.TwistedBrother;
import com.wl.TwistedSister;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;

public class LotteryWithRepetitionsBenchmark {
  @State(Scope.Thread)
  public static class SimpleState extends LotteryState {
    @NotNull
    IntLottery newLottery(double[] weights) {
      return new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class AliasState extends LotteryState {

    @NotNull
    IntLottery newLottery(double[] weights) {
      return new AliasLottery(weights, ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class TwistedAliasState extends LotteryState {
    @Override
    IntLottery newLottery(double[] weights) {
      return new TwistedAliasLottery(weights, ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class TwistedSisterState extends LotteryState {
    @Override
    IntLottery newLottery(double[] weights) {
      return new TwistedSister(weights, ThreadLocalRandom::current);
    }
  }

  @State(Scope.Thread)
  public static class TwistedBrotherState extends LotteryState {
    @Override
    IntLottery newLottery(double[] weights) {
      return new TwistedBrother(weights, ThreadLocalRandom::current);
    }
  }


  @Benchmark
  public int simple(SimpleState state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int alias(AliasState state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int twistedAlias(TwistedAliasState state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int twistedSister(TwistedSisterState state) {
    return benchmarkImpl(state);
  }

  @Benchmark
  public int twistedBrother(TwistedBrotherState state) {
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
