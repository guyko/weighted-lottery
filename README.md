Multiple implementations of weighted lottery, in java or kotlin.

Using a weighted lottery implementation, with repetitions:
```
val weights = arrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLottery(weights)
val index = lottery.draw()
```
Using a weighted lottery implementation, without repetitions:
```
val weights = arrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLotteryNoRepetitions(weights)
// every index is returned exactly once
while(lottery.remaining() > 0) {
  val index = lottery.draw()
  // so something
}
```

all implementations are benchmarked using JMH, and http://jmh.morethan.io/ for visualization:

![Alt text](/benchmark.png "Benchmark")

