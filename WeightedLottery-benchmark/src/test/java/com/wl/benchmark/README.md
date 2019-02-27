# Benchmarking weighted lotteries

All implementations are benchmarked using [jmh](http://tutorials.jenkov.com/java-performance/jmh.html), with gc profiler enabled
The interesting metric is the average time for performing either one sample (draw k from n), or many samples (m times draw k from n). In addition, we measure gc metrics like allocation rate and gc count and duration

Latest benchmark result can be found [here](https://jmh.morethan.io/?source=https://raw.githubusercontent.com/guyko/weighted-lottery/master/jmh-result.json)

![alt text](https://github.com/guyko/weighted-lottery/blob/master/benchmark.jpg)

A typical benchmark class will look like this:

```Java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class OneSampleWithRepetitionsRandomDistBenchmark {

  private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;

  @Benchmark
  public void simple() {
    double[] weights = utils.getRandomWeights();
    utils.drawKTimes(new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current));
  }
}
```
The _'simple'_ method uses the _WeightedLotteryBenchmark_ utility to obtain random weights, and to draw k times by calling _'drawKTimes'_. 


## Use cases
### _'with repetitions'_ vs _'without repetitions'_ 
Saperated benchmark classes for the two types of implementations

### _'init'_ vs _'draw'_ 
Eech benchmark is executed to measure preprocessing phase and draw phase separately and together

### One sample of k items vs many samples of k items
Some implementations may be better when reusing the same state when weights are the same.  Using _'mTimesDrawKTimes'_ with a lambda, makes it possible to keep that state and reuse it (in this case, the state is the lottery instance)

```Java
double[] weights = utils.getRandomWeights();
SimpleIntWeightedLottery lottery = new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current);
benchmark.mTimesDrawKTimes(() -> lottery);
```

### Uniform vs exponential distribution of probabilities 
Using _WeightedLotteryBenchmark_ utility, one can obtain both random and exponential weights

```Java
double[] weights = utils.getPowerWeights(); // [0.9^1, 0.9^2, 0.9^3, ...]
benchmark.drawKTimes(new SimpleIntWeightedLottery(weights, ThreadLocalRandom::current));
```
## FAQ
### I wrote a new lottery class, which benchmark should I add it to ?
You should add a method to either _LotteryNoRepetitionsBenchmark_ or _LotteryWithRepetitionsBenchmark_. We will execute the benchmark after merging your pull request 

### How do I execute the benchmark on my machine ?
The main method of _WeightedLotteryBenchmark_ executes all use cases, and prints the outcome to jmh-result.json (note that it may take few hours to finish).

You can visualize the outcome by uploading the file to http://jmh.morethan.io/

Another option is to install [JMH IntelliJ plugin](https://plugins.jetbrains.com/plugin/7529-jmh-plugin) and use it to run a single benchmark class by right clicking the class -> Run

