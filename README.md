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
```Kotlin
Using a weighted lottery implementation, without repetitions - you can potentially drain the items to draw:
```
val weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLotteryNoRepetitions(weights)
while(lottery.remaining() > 0) {
  val index = lottery.draw()
  // do something
}
```

all implementations are benchmarked using JMH, and http://jmh.morethan.io/ for visualization:

![Alt text](/benchmark.png "Benchmark")

