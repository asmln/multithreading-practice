package com.github.asmln.multi.producer_consumer;

import java.util.ArrayDeque;
import java.util.Queue;

// Демонстрация проблемы.
public class BadMarket implements Market {

    private final int maxSize;
    private final Queue<Integer> buffer;

    public BadMarket(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ArrayDeque<>(maxSize);
    }

    @Override
    public String description() {
        return "Обменник без синхронизации";
    }

    @Override
    public int get() {
        while (buffer.isEmpty()) {
            // Надо обработать прерывание, иначе выполнение просто зависнет
            if (Thread.currentThread().isInterrupted()) {
                return -1;
            }
        }
        return buffer.poll();
    }

    @Override
    public void put(int n) {
        while (buffer.size() >= maxSize) {
            // Надо обработать прерывание, иначе выполнение просто зависнет
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
        buffer.offer(n);
    }
}
