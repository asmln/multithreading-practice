package com.github.asmln.multi.producer_consumer;

public interface Market {
    String description();
    int get();
    void put(int n);
}
