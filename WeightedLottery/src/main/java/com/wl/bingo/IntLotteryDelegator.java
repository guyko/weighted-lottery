package com.wl.bingo;

import com.wl.IntLottery;

public interface IntLotteryDelegator extends IntLottery {
  IntLotteryDelegator delegate();
}
