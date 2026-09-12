package com.github.asmln.multi.counter;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class Main {

    static List<Counter> COUNTERS = List.of(
            new BadCounter(),
            new BadVolatileCounter(),
            new SynchronizedCounter(),
            new ReentrantLockCounter(),
            new AtomicCounter(),
            new AdderCounter(),
            new AccumulatorCounter()
    );

    static void main() {
        int threadsCount = 100;
        int stepCount = 10000;
        for (var counter: COUNTERS) {
            runMultiThreads(threadsCount, stepCount, counter);
            IO.println(counter.description() + String.format(
                    Locale.FRANCE,
                    ": %s Ожидаем %,d, получили %,d.",
                    (threadsCount * stepCount == counter.get() ? "✅" : "❌"),
                    (threadsCount * stepCount),
                    counter.get())
            );
        }
    }

    static void runMultiThreads(int threadsCount, int stepCount, Counter counter) {
        IntStream.rangeClosed(1, threadsCount)
                .parallel()
                .forEach(n -> {
                    for (int i = 0; i < stepCount; i++) {
                        counter.increment();
                    }
                });
    }
}
