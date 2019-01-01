# Benchmarking weighted lotteries

All implementations are benchmarked using [jmh](http://tutorials.jenkov.com/java-performance/jmh.html), with gc profiler enabled

A typical benchmark will look like this:

```Java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class OneSampleWithRepetitionsRandomDistBenchmark {

  private static WeightedLotteryBenchmark benchmark = WeightedLotteryBenchmark.INSTANCE;

  @Benchmark
  public void simple() {
    double[] randomWeights = benchmark.getRandomWeights();
    benchmark.drawKTimes(new SimpleIntWeightedLottery(randomWeights, ThreadLocalRandom::current));
  }
}
```
The _'simple'_ method uses the _WeightedLotteryBenchmark_ utility to obtain random weights, and to draw k times. 



### Use cases
* _'with repetitions'_ vs _'without repetitions'_
* one sample of k items vs many samples of k items (some implementations may be better when reusing the same weights)
* uniform vs exponential distribution _(0.5, 0.25, 0.125, .....)_ of probabilities
