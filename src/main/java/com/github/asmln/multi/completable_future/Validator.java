package com.github.asmln.multi.completable_future;

import com.github.asmln.multi.completable_future.integration.ExternalValidationService;

import java.util.concurrent.Executor;

public abstract class Validator {
    private final ExternalValidationService externalValidationService = new ExternalValidationService();
    private final Executor executor;

    protected Validator(Executor executor) {
        this.executor = executor;
    }

    protected abstract String description();
    protected abstract boolean validateExternal();

    public void validate() {
        long start = System.currentTimeMillis();
        boolean ok = validateExternal();
        long time = System.currentTimeMillis() - start;
        IO.println(
                String.format("%s: %s, длительность (мс) = %d.", description(), ok, time)
        );
    }

    protected ExternalValidationService getExternalValidationService() {
        return externalValidationService;
    }

    protected Executor executor() {
        return executor;
    }
}
