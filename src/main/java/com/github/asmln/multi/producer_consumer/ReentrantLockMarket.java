package com.github.asmln.multi.producer_consumer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Producer-Consumer с ReentrantLock и двумя условиями (read/write)
public class ReentrantLockMarket implements Market {
    private final int maxSize;
    private final Queue<Integer> buffer;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition readCondition = lock.newCondition();
    private final Condition writeCondition = lock.newCondition();

    public ReentrantLockMarket(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ArrayDeque<>(maxSize);
    }

    @Override
    public String description() {
        return "ReentrantLock и 2 условия (read/write)";
    }

    @Override
    public int get() {
        lock.lock();
        try {
            while (buffer.isEmpty()) {
                readCondition.await();
            }
            writeCondition.signalAll();
            return buffer.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(int n) {
        lock.lock();
        try {
            while (buffer.size() >= maxSize) {
                writeCondition.await();
            }
            readCondition.signalAll();
            buffer.offer(n);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
    }
}
