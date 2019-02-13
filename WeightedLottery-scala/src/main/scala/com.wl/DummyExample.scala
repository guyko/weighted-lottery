package com.wl

class DummyExample extends IntLottery {
  override def draw(): Int = 0

  override def empty(): Boolean = false

  override def remaining(): Int = 0
}