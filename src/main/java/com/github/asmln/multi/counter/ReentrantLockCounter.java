package com.github.asmln.multi.counter;

import java.util.concurrent.locks.ReentrantLock;

// Счётчик на ReentrantLock.
// Это работает, но для счётчиков так делать не надо.
public class ReentrantLockCounter implements Counter {
    private int count = 0; // volatile не нужен - блокировка на мониторе даёт нужный Happens Before.
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public String description() {
        return "ReentrantLock";
    }
    @Override
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public int get() {
        return count;
    }
}
