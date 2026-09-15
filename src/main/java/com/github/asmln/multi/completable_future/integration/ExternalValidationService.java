package com.github.asmln.multi.completable_future.integration;

public class ExternalValidationService {
    public boolean validate(ValidationCase validationCase) {
        return validationCase.result();
    }
}
