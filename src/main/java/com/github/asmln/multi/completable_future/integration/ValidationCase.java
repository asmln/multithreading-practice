package com.github.asmln.multi.completable_future.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class ValidationCase {
    private final int timeout;
    private final boolean result;
    private final Class<? extends RuntimeException> exception;

    private ValidationCase(int timeout, boolean result, Class<? extends RuntimeException> exception) {
        this.timeout = timeout;
        this.result = result;
        this.exception = exception;
    }

    public ValidationCase(int timeout, boolean result) {
        this(timeout, result, null);
    }

    public ValidationCase(int timeout, Class<? extends RuntimeException> exception) {
        this(timeout, false, exception);
    }

    public boolean result() {
        if (timeout > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted.");
            }
        }
        if (exception != null) {
            try {
                throw exception.getConstructor(String.class).newInstance("Validation exception.");
            } catch (Exception e) {
                throw new RuntimeException("Exception when throw Exception.");
            }
        }
        return result;
    }
}
