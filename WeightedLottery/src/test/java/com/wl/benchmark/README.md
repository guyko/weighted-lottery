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
    double[] weights = benchmark.getRandomWeights();
    benchmark.drawKTimes(new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current));
  }
}
```
The _'simple'_ method uses the _WeightedLotteryBenchmark_ utility to obtain random weights, and to draw k times by calling _'drawKTimes'_. 


## Use cases
### _'with repetitions'_ vs _'without repetitions'_ 
Saperated benchmark classes for the two types of implementations

### One sample of k items vs many samples of k items
Some implementations may be better when reusing the same state when weights are the same.  Using _'mTimesDrawKTimes'_ with a lambda, makes it possible to keep that state and reuse it (in this case, the state is the lottery instance)

```Java
@Benchmark
public void simple() {
  double[] weights = benchmark.getRandomWeights();
  SimpleIntWeightedLottery lottery = new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current);
  benchmark.mTimesDrawKTimes(() -> lottery);
}
```

### Uniform vs exponential distribution of probabilities 
Using _WeightedLotteryBenchmark_ utility, one can obtain both random and exponential weights

```Java
double[] weights = benchmark.getPowerWeights(); // [0.5, 0.25, 0.125, ...]
benchmark.drawKTimes(new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current));
```
## FAQ
### I wrote a new lottery class, which benchmark should I add it to ?
If your class supports repetitions, you should add a method to the following classes: 

* _OneSamplesWithRepetitionsRandomDistBenchmark_
* _OneSamplesWithRepetitionsPowerDistBenchmark_
* _ManySamplesWithRepetitionsRandomDistBenchmark_
* _ManySamplesWithRepetitionsPowerDistBenchmark_

Otherwise, you should add it to:

* _OneSamplesNoRepetitionsRandomDistBenchmark_
* _OneSamplesNoRepetitionsPowerDistBenchmark_
* _ManySamplesNoRepetitionsRandomDistBenchmark_
* _ManySamplesNoRepetitionsPowerDistBenchmark_

