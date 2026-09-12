package com.github.asmln.multi.counter;

// Демонстрация проблемы.
// Используем волшебное слово volatile и убеждаемся, что оно не помогает в этом случае.
public class BadVolatileCounter implements Counter {
    private volatile int count = 0;

    @Override
    public String description() {
        return "volatile int";
    }
    @Override
    public void increment() {
        count++;
    }
    @Override
    public int get() {
        return count;
    }
}
