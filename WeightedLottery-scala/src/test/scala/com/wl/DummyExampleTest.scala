package com.wl

@org.junit.Ignore
class DummyExampleTest extends WeightedLotteryNoRepetitionsTestBase {


  override def weightedLottery(weights: Array[Double]): IntLottery = {
    new DummyExample()
  }


}