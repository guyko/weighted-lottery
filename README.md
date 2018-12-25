Multiple implementations of weighted lottery, in java or kotlin

to be used in the following manner:

```
val weights = arrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLottery(weights)
val index = lottery.draw()
```

all implementations are benchmarked using JMH, and http://jmh.morethan.io/ for visualization:

![Alt text](/benchmark.png "Benchmark")

