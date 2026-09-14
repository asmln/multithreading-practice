package com.github.asmln.multi.serial_parallel.integration;

import java.util.concurrent.TimeUnit;

// Сервис проверки платежа.
public class PaymentService {
    private static final int TIMEOUT = 1;

    public boolean checkPaymentById(long paymentId) {
        try {
            TimeUnit.SECONDS.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return true;
    }
}
