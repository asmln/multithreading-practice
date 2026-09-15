package com.github.asmln.multi.completable_future;

import java.util.List;

public class Main {
    static final List<Validator> validators = List.of(
            new OkValidator()
    );

    static void main() {
        IO.println("\uD83D\uDCA1CompletableFuture cases");
        for (var validator: validators) {
            validator.validate();
        }
    }
}
