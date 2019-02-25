# [weighted-lottery](https://github.com/guyko/weighted-lottery) [![Build Status](https://api.travis-ci.org/guyko/weighted-lottery.png?branch=master)](https://api.travis-ci.org/guyko/weighted-lottery) [![Apache License V.2](https://img.shields.io/badge/license-Apache%20V.2-blue.svg)](https://github.com/guyko/weighted-lottery/blob/master/LICENSE) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.guyko/WeightedLottery/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.guyko/WeightedLottery)

Multiple implementations of weighted lottery, in java or kotlin.

Weighted lottery deals allows to randomly select items according to given probabilities, with or without repetitions, in the following manner:

```Kotlin
val weights = doubleArrayOf(0.15, 0.0, 0.2, 0.0, 0.65)
val lottery = SimpleWeightedLottery(weights)
(0 until k).forEach {
  val index = lottery.draw()
  // do something
}
```

For more details on weighted-lottory, follow the [wiki page](https://github.com/guyko/weighted-lottery/wiki)

All implementations are benchmarked using JMH, and can be found [here](https://jmh.morethan.io/?source=https://raw.githubusercontent.com/guyko/weighted-lottery/master/jmh-result.json)

![alt text](https://github.com/guyko/weighted-lottery/blob/master/benchmark.jpg)

The benchmark visualization is done with http://jmh.morethan.io/

