package com.wl.bingo;

import com.wl.IntLottery;

import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class Bingo implements IntLottery {
  private IntLotteryDelegator delegator;

  public Bingo(final double[] weights, final ThreadLocalRandom random) {
    delegator = new PartialSumsTree(weights, new boolean[weights.length], weights.length, random);
  }

  @Override
  public int draw() {
    if (empty()) {
      throw new NoSuchElementException();
    }
    final int result = delegator.draw();
    if (result >= 0) {
      return result;
    }
    delegator = delegator.delegate();
    return delegator.draw();
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
