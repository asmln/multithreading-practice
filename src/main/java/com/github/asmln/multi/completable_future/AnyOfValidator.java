package com.github.asmln.multi.completable_future;

import com.github.asmln.multi.completable_future.integration.ValidationCase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

// UseCase: Нужно выполнить несколько запросов и дождаться результата самого быстрого.
// Большинство запросов выполняются DURATION мс.
// Один - SHORT_DURATION мс.
// После получения ответа прерываем выполнение задач.
public class AnyOfValidator extends Validator {
    private final static int DURATION = 1000;
    private final static int SHORT_DURATION = 100;

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
                                        new ValidationCase(DURATION, true)
                                ),
                                executor()
                        )
                ).toList();
        var array = fList.toArray(new CompletableFuture[4]);
        array[3] = CompletableFuture.supplyAsync(
                () -> getExternalValidationService().validate(
                        new ValidationCase(SHORT_DURATION, false)
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
