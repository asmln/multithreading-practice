package com.github.asmln.multi.counter;

// Счётчик на synchronized.
// Это работает, но для счётчиков так делать не надо.
public class SynchronizedCounter implements Counter {
    private int count = 0; // volatile не нужен - блокировка на мониторе даёт нужный Happens Before.

    @Override
    public String description() {
        return "synchronized";
    }
    @Override
    public synchronized void increment() {
        count++;
    }
    @Override
    public int get() {
        return count;
    }
}
