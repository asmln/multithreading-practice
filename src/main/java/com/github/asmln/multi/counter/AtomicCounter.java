package com.github.asmln.multi.counter;

import java.util.concurrent.atomic.AtomicInteger;

// Счётчик на Atomic.
public class AtomicCounter implements Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public String description() {
        return "Atomic";
    }
    @Override
    public synchronized void increment() {
        count.incrementAndGet();
    }
    @Override
    public synchronized int get() {
        return count.get();
    }
}
