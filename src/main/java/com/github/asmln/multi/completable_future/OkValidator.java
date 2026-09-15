package com.github.asmln.multi.completable_future;

import com.github.asmln.multi.completable_future.integration.ValidationCase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

// UseCase: Нужно выполнить несколько запросов и дождаться всех результатов.
// Каждый запрос выполняется TIMEOUT мс.
// Последовательно их выполнять слишком долго - делаем параллельно.
public class OkValidator extends Validator {
    private final static int DURATION = 1000;
    private final static int STEPS = 4;

    protected OkValidator(Executor executor) {
        super(executor);
    }

    @Override
    public String description() {
        return String.format("Валидация из %d шагов, каждый %d мс (true)", STEPS, DURATION);
    }

    @Override
    public boolean validateExternal() {
        List<CompletableFuture<Boolean>> fList = IntStream.range(0, STEPS)
                .mapToObj(
                        _ -> CompletableFuture.supplyAsync(
                                () -> getExternalValidationService().validate(
                                        new ValidationCase(DURATION, true)
                                ),
                                executor()
                        )
                ).toList();
        CompletableFuture.allOf(fList.toArray(new CompletableFuture[STEPS])).join();
        return fList.stream().allMatch(CompletableFuture::join);
    }
}
