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
        int stepCount = 10_000;
        IO.println("\uD83D\uDCA1Счётчик");
        // Работа в одном потоке:
        IO.println("➡️ Однопоточная работа:");
        BadCounter badCounter = new BadCounter();
        for (int i = 0; i < threadsCount * stepCount; i++) {
            badCounter.increment();
        }
        IO.println("Работа в одном потоке" + String.format(
                Locale.FRANCE,
                ": %s Ожидаем %,d, получили %,d.",
                (threadsCount * stepCount == badCounter.get() ? "✅" : "❌"),
                (threadsCount * stepCount),
                badCounter.get())
        );
        // Многопоточка:
        IO.println("\uD83D\uDD00 Многопоточная работа:");
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
                .forEach(_ -> {
                    for (int i = 0; i < stepCount; i++) {
                        counter.increment();
                    }
                });
    }
}
