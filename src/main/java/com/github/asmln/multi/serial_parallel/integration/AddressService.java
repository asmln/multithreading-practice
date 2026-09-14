package com.github.asmln.multi.serial_parallel.integration;

import com.github.asmln.multi.serial_parallel.dto.Address;

import java.util.concurrent.TimeUnit;

// Сервис получения адреса клиента.
public class AddressService {
    private static final int TIMEOUT = 1;

    public Address obtainAddressByClientId(long clientId) {
        try {
            TimeUnit.SECONDS.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new Address("г. Москва", "ул. Абрикосовая", "16а");
    }
}
