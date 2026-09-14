package com.github.asmln.multi.serial_parallel.dto;

public record Order(
        long clientId,
        long paymentId,
        long productId
) {
}
