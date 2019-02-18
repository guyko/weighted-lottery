package com.wl.benchmark;

import com.wl.AliasLottery;
import com.wl.IntLottery;
import com.wl.LotteryTestUtils;
import com.wl.SimpleIntWeightedLottery;
import com.wl.TwistedAliasLottery;
import com.wl.TwistedBrother;
import com.wl.TwistedSister;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ManySamplesWithRepetitionsRandomDistBenchmark {

    private static LotteryTestUtils utils = LotteryTestUtils.INSTANCE;

    @Benchmark
    public void simple() {
        SimpleIntWeightedLottery lottery = new SimpleIntWeightedLottery(utils.getRandomWeights(), ThreadLocalRandom::current);
        utils.mTimesDrawKTimes(() -> lottery);
    }

    @Benchmark
    public void alias() {
        AliasLottery lottery = new AliasLottery(utils.getRandomWeights(), ThreadLocalRandom::current);
        utils.mTimesDrawKTimes(() -> lottery);
    }

    @Benchmark
    public void twistedAlias() {
        IntLottery lottery = new TwistedAliasLottery(utils.getRandomWeights(), ThreadLocalRandom::current);
        utils.mTimesDrawKTimes(() -> lottery);
    }

    @Benchmark
    public void twistedSister() {
        IntLottery lottery = new TwistedSister(utils.getRandomWeights(), ThreadLocalRandom::current);
        utils.mTimesDrawKTimes(() -> lottery);
    }

    @Benchmark
    public void twistedBrother() {
        IntLottery lottery = new TwistedBrother(utils.getRandomWeights(), ThreadLocalRandom::current);
        utils.mTimesDrawKTimes(() -> lottery);
    }
}
