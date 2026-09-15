package com.github.asmln.multi.completable_future;

import com.github.asmln.multi.completable_future.integration.ExternalValidationService;
import com.github.asmln.multi.completable_future.integration.ValidationCase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class OkValidator extends Validator {
    private final static int TIMEOUT = 1000;
    private final ExternalValidationService externalValidationService = new ExternalValidationService();

    @Override
    public String description() {
        return String.format("Валидация из четырёх шагов, каждый %d мс (true)", TIMEOUT);
    }

    @Override
    public boolean validateExternal() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Boolean>> fList = IntStream.range(0, 4)
                    .mapToObj(
                            _ -> CompletableFuture.supplyAsync(
                                    () -> externalValidationService.validate(
                                            new ValidationCase(TIMEOUT, true)
                                    ),
                                    executor
                            )
                    ).toList();
            CompletableFuture.allOf(fList.toArray(new CompletableFuture[4])).join();
            return fList.stream().allMatch(CompletableFuture::join);
        }
    }
}
