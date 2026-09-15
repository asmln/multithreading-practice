package com.github.asmln.multi.completable_future;

import java.util.List;
import java.util.concurrent.Executors;

public class Main {
    static void main() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Validator> validators = List.of(
                    new OkValidator(executor),
                    new AnyOfValidator(executor)
            );
            IO.println("\uD83D\uDCA1CompletableFuture cases");
            for (var validator : validators) {
                validator.validate();
            }
        }
    }
}
