package com.wl;

import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class SumTreeLottery implements IntLottery {
  private IntLotteryDelegator delegator;

  public SumTreeLottery(final double[] weights, final ThreadLocalRandom random) {
    delegator = new PartialSumsTree(weights, new boolean[weights.length], weights.length, random);
  }

  @Override
  public int draw() {
    if (empty()) {
      throw new NoSuchElementException();
    }
    int result = delegator.draw();
    if (result >= 0) {
      return result;
    }
    delegator = delegator.delegate();
    while ((result = delegator.draw()) < 0);
    return result;
  }

  @Override
  public boolean empty() {
    return delegator.empty();
  }

  @Override
  public int remaining() {
    return delegator.remaining();
  }
}
