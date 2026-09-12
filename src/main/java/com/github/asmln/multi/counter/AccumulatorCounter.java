package com.github.asmln.multi.counter;

import java.util.concurrent.atomic.LongAccumulator;

// Счётчик на LongAccumulator.
// В данном случае избыточно использовать LongAccumulator, т.к. он больше подходит для функции, отличной от добавления 1.
public class AccumulatorCounter implements Counter {
    private final LongAccumulator count = new LongAccumulator(Long::sum, 0);

    @Override
    public String description() {
        return "LongAccumulator";
    }
    @Override
    public synchronized void increment() {
        count.accumulate(1);
    }
    @Override
    public synchronized int get() {
        return count.intValue();
    }
}
