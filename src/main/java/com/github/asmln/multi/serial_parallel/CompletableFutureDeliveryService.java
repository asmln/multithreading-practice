package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Address;
import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;

import java.util.concurrent.*;

public class CompletableFutureDeliveryService extends AbstractDeliveryService {
    @Override
    public String description() {
        return "CompletableFuture";
    }
    @Override
    public Delivery createDelivery(Order order) {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
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
            CompletableFuture<Delivery> deliveryCF = paymentFuture.thenCombine(
                    productFuture,
                    (paymentOk, productOk) -> paymentOk && productOk
            ).thenCombine(
                    addressFuture,
                    (ok, address) -> {
                        if (ok && address != null) {
                            return new Delivery(order.clientId(), address);
                        } else {
                            throw new IllegalStateException();
                        }
                    }
            );
            try {
                return deliveryCF.join();
            } catch (CompletionException e) {
                throw new IllegalStateException();
            }
        }
    }
}
