package com.github.asmln.multi.completable_future;

public abstract class Validator {
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
}
