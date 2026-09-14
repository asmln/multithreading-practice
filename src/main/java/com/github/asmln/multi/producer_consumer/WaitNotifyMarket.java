package com.github.asmln.multi.producer_consumer;

import java.util.ArrayDeque;
import java.util.Queue;

// Синхронизированный Producer-Consumer
public class WaitNotifyMarket implements Market {
    private final int maxSize;
    private final Queue<Integer> buffer;

    public WaitNotifyMarket(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ArrayDeque<>(maxSize);
    }

    @Override
    public String description() {
        return "synchronized и wait-notify";
    }

    @Override
    public synchronized int get() {
        while (buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException();
            }
        }
        notifyAll();
        return buffer.poll();
    }

    @Override
    public synchronized void put(int n) {
        while (buffer.size() >= maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException();
            }
        }
        buffer.offer(n);
        notifyAll();
    }
}
