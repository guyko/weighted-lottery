package com.wl.benchmark;

import com.wl.IntLottery;
import com.wl.LotteryTestUtils;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public abstract class LotteryState {
  private IntLottery lottery;

  @Param({"Uniform", "Exponential"})
  EWeightsDistribution distribution;
  @Param({"Init", "DrawK", "InitAndDrawK"})
  EBenchmarkType benchmarkType;

  @Setup(Level.Invocation)
  public void doSetup() {
    lottery = benchmarkType == EBenchmarkType.DrawK ? newLottery(getWeights()) : null;
  }

  abstract IntLottery newLottery(double[] weights);

  IntLottery getLottery() {
    return lottery == null ? newLottery(getWeights()) : lottery;
  }


  private double[] getWeights() {
    return distribution == EWeightsDistribution.Uniform ? LotteryTestUtils.INSTANCE.getRandomWeights() : LotteryTestUtils.INSTANCE.getPowerWeights();
  }
}
