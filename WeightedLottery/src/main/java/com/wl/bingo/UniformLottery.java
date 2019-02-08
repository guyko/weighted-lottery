package com.wl.bingo;

import java.util.concurrent.ThreadLocalRandom;

public class UniformLottery implements IntLotteryDelegator {
  private final ThreadLocalRandom random;
  private int size;
  private int[] indexes;

  public UniformLottery(final int[] indexes, final int size, final ThreadLocalRandom random) {
    this.random = random;
    this.size = size;
    this.indexes = indexes;
  }

  public int draw() {
    int i = size - random.nextInt(size) - 1;
    final int randomIndex = indexes[i];
    indexes[i] = indexes[--size];
    return randomIndex;
  }

  @Override
  public boolean empty() {
    return size == 0;
  }

  @Override
  public int remaining() {
    return size;
  }

  @Override
  public IntLotteryDelegator delegate() {
    return this;
  }
}
