package com.github.asmln.multi.producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueMarket implements Market {

    private final BlockingQueue<Integer> buffer;

    public BlockingQueueMarket(int maxSize) {
        this.buffer = new ArrayBlockingQueue<>(maxSize);
    }

    @Override
    public String description() {
        return "BlockingQueue";
    }

    @Override
    public int get() {
        try {
            return buffer.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        }
    }

    @Override
    public void put(int n) {
        try {
            buffer.put(n);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        }
    }
}
