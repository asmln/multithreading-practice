package com.github.asmln.multi.counter;

// Демонстрация проблемы.
// Считаем в обычном поле типа int.
public class BadCounter implements Counter {
    private int count = 0;

    @Override
    public String description() {
        return "Обычное поле int";
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
