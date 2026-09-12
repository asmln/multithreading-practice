package com.github.asmln.multi.counter;

import java.util.concurrent.atomic.LongAdder;

// Счётчик на LongAdder.
// Интересная структура, которая заточена под высокую конкуренцию.
// Внутри устроен как массив ячеек, потоки пишут в разные ячейки.
// За это расплачиваемся суммированием при получении результата
// и бОльшим объёмом необходимой памяти, чем AtomicInteger.
public class AdderCounter implements Counter {
    private final LongAdder count = new LongAdder();

    @Override
    public String description() {
        return "LongAdder";
    }
    @Override
    public synchronized void increment() {
        count.increment();
    }
    @Override
    public synchronized int get() {
        return count.intValue();
    }
}
