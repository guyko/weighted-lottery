# [weighted-lottery](https://github.com/guyko/weighted-lottery) [![Build Status](https://api.travis-ci.org/guyko/weighted-lottery.png?branch=master)](https://api.travis-ci.org/guyko/weighted-lottery) [![Apache License V.2](https://img.shields.io/badge/license-Apache%20V.2-blue.svg)](https://github.com/guyko/weighted-lottery/blob/master/LICENSE)

Multiple implementations of weighted lottery, in java or kotlin.

Weighted lottery deals allows to randomly select items according to given probabilities, with or without repetitions

Using a weighted lottery implementation, with repetitions:
```Kotlin
val weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLottery(weights)
(0 until k).forEach {
  val index = lottery.draw()
  // do something
}
```
Using a weighted lottery implementation, without repetitions - you can potentially drain the items to draw:
```Kotlin
val weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLotteryNoRepetitions(weights)
while(lottery.remaining() > 0) {
  val index = lottery.draw()
  // do something
}
```

All implementations are benchmarked using JMH, and can be found [here](https://github.com/guyko/weighted-lottery/blob/master/jmh-result.json)

The following visualization is taken from using http://jmh.morethan.io/ with the latest benchmark:

![Alt text](/benchmark.png "Benchmark")

