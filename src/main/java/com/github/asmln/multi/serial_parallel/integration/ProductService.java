package com.github.asmln.multi.serial_parallel.integration;

import java.util.concurrent.TimeUnit;

// Сервис проверки доступности товара.
public class ProductService {
    private static final int TIMEOUT = 1;

    public boolean checkAvailabilityByProductId(long productId) {
        try {
            TimeUnit.SECONDS.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return true;
    }
}
