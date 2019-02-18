package com.wl.benchmark;

import com.wl.AliasLottery;
import com.wl.IntLottery;
import com.wl.LotteryTestUtils;
import com.wl.SimpleIntWeightedLottery;
import com.wl.TwistedAliasLottery;
import com.wl.TwistedBrother;
import com.wl.TwistedSister;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ManySamplesWithRepetitionsPowerDistBenchmark {

  private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;

  @Benchmark
  public void simple() {
    IntLottery lottery = new SimpleIntWeightedLottery(utils.getPowerWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }

  @Benchmark
  public void alias() {
    IntLottery lottery = new AliasLottery(utils.getPowerWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }

  @Benchmark
  public void twistedAlias() {
    IntLottery lottery = new TwistedAliasLottery(utils.getPowerWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }
  @Benchmark
  public void twistedSister() {
    IntLottery lottery = new TwistedSister(utils.getPowerWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }
  @Benchmark
  public void twistedBrother() {
    IntLottery lottery = new TwistedBrother(utils.getPowerWeights(), ThreadLocalRandom::current);
    utils.mTimesDrawKTimes(() -> lottery);
  }
}
