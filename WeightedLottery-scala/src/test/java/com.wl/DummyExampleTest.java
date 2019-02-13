package com.wl;

import org.jetbrains.annotations.NotNull;

@org.junit.Ignore
public class DummyExampleTest extends WeightedLotteryWithRepetitionsTestBase {
  @NotNull
  public IntLottery weightedLottery(@NotNull double[] weights) {
    return new DummyExample();
  }
}
