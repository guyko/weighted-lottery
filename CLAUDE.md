# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a weighted lottery library written in Java and Kotlin that provides multiple implementations for randomly selecting items according to given probabilities, with or without repetitions. All implementations are benchmarked using JMH (Java Microbenchmark Harness).

## Project Structure

- **WeightedLottery/**: Core library module containing lottery implementations
  - `src/main/java/com/wl/`: Main implementations (Kotlin and Java)
  - `src/test/java/com/wl/`: Unit tests
- **WeightedLottery-benchmark/**: JMH benchmarking module
  - `src/test/java/com/wl/benchmark/`: Benchmark implementations

## Build Commands

This is a Maven multi-module project using Java 8 and Kotlin 1.3.0.

### Build and Test
```bash
mvn clean compile                    # Compile all modules
mvn test                            # Run unit tests
mvn clean package                   # Build JARs
```

### Benchmarks
```bash
# Run all benchmarks (takes several hours)
cd WeightedLottery-benchmark
mvn exec:java -Dexec.mainClass="com.wl.benchmark.WeightedLotteryBenchmarkKt"

# Results are written to jmh-result.json in root directory
# Visualize at: http://jmh.morethan.io/
```

### Individual Benchmark Classes
Use JMH IntelliJ plugin to run individual benchmark classes by right-clicking -> Run

## Core Architecture

### IntLottery Interface
All lottery implementations implement `IntLottery` interface with methods:
- `draw()`: Returns random index according to distribution
- `empty()`: Returns true if no items left to draw
- `remaining()`: Returns number of valid calls to draw

### Implementation Categories

**With Repetitions** (samples can be drawn multiple times):
- `SimpleIntWeightedLottery`: Binary search on accumulated weights
- `AliasLottery`: Alias method implementation
- `TwistedAliasLottery`: Optimized alias method variant
- `SumTreeLottery`: Sum tree based implementation
- `UniformLottery`: Simple uniform distribution

**Without Repetitions** (each sample drawn only once):
- `SimpleIntWeightedLotteryNoRepetitions`: Removes drawn items
- `AliasLotteryNoRepetitions`: Alias method with removal
- `ReservoirLottery`: Reservoir sampling based

### Test Structure

- `LotteryTestBase`: Abstract base for testing with repetitions
- `WeightedLotteryNoRepetitionsTestBase`: Abstract base for testing without repetitions
- `WeightedLotteryWithRepetitionsTestBase`: Additional test patterns
- `LotteryTestUtils`: Utility methods and test data (random/power distributions)

All test classes extend appropriate base classes and implement abstract methods to provide specific lottery instances.

### Benchmark Structure

- `LotteryWithRepetitionsBenchmark`: Benchmarks for sampling with repetitions
- `LotteryNoRepetitionsBenchmark`: Benchmarks for sampling without repetitions
- Separate measurement of initialization vs. draw phases
- Different weight distributions (uniform random vs. exponential power)
- Single sample vs. multiple samples scenarios

## Language Usage

- **Kotlin**: Primary language for new implementations and tests
- **Java**: Used for some core utilities and legacy implementations
- Mixed compilation is configured in Maven with Kotlin-first compilation order

## Development Patterns

- New lottery implementations should implement `IntLottery` interface
- Follow existing naming pattern: `*Lottery` for with-repetitions, `*LotteryNoRepetitions` for without
- Add corresponding test class extending appropriate base class
- Add benchmark methods to relevant benchmark class
- Validate input weights (no NaN, no negative values)
- Use ThreadLocalRandom for random number generation