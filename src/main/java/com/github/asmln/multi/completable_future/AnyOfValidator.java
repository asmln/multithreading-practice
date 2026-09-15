package com.github.asmln.multi.completable_future;

import com.github.asmln.multi.completable_future.integration.ValidationCase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class AnyOfValidator extends Validator {
    private final static int TIMEOUT = 1000;

    protected AnyOfValidator(Executor executor) {
        super(executor);
    }

    @Override
    public String description() {
        return "Валидация из 4 шагов, используем самый быстрый (false)";
    }

    @Override
    public boolean validateExternal() {
        List<CompletableFuture<Boolean>> fList = IntStream.range(0, 3)
                .mapToObj(
                        _ -> CompletableFuture.supplyAsync(
                                () -> getExternalValidationService().validate(
                                        new ValidationCase(TIMEOUT, true)
                                ),
                                executor()
                        )
                ).toList();
        var array = fList.toArray(new CompletableFuture[4]);
        array[3] = CompletableFuture.supplyAsync(
                () -> getExternalValidationService().validate(
                        new ValidationCase(100, false)
                ),
                executor()
        );
        try {
            return (Boolean) CompletableFuture.anyOf(array).join();
        } finally {
            for (CompletableFuture<?> f : array) {
                f.cancel(true);
            }
        }
    }
}
