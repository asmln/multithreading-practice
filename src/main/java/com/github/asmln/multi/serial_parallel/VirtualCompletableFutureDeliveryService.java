package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Address;
import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualCompletableFutureDeliveryService extends AbstractDeliveryService {
    @Override
    public String description() {
        return "Virtual Threads и CompletableFuture";
    }
    @Override
    public Delivery createDelivery(Order order) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<Boolean> paymentFuture = CompletableFuture.supplyAsync(
                    () -> getPaymentService().checkPaymentById(order.paymentId()),
                    executor
            );
            CompletableFuture<Boolean> productFuture = CompletableFuture.supplyAsync(
                    () -> getProductService().checkAvailabilityByProductId(order.productId()),
                    executor
            );
            CompletableFuture<Address> addressFuture = CompletableFuture.supplyAsync(
                    () -> getAddressService().obtainAddressByClientId(order.clientId()),
                    executor
            );
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    paymentFuture,
                    productFuture,
                    addressFuture
            );
            allFutures.join();
            Address address = addressFuture.join();
            if (Boolean.TRUE.equals(paymentFuture.join())
                    && Boolean.TRUE.equals(productFuture.join())
                    && address != null) {
                return new Delivery(order.clientId(), address);
            } else {
                throw new IllegalStateException();
            }
        } catch (CompletionException e) {
            throw new IllegalStateException();
        }
    }
}
