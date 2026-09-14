package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Address;
import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;

public class ThreadsDeliveryService extends AbstractDeliveryService {

    private static class ResultContainer<T> {
        volatile T value;
    }

    @Override
    public String description() {
        return "Threads";
    }
    @Override
    public Delivery createDelivery(Order order) {
        var paymentResult = new ResultContainer<Boolean>();
        Thread thread1 = new Thread(
                () -> paymentResult.value = getPaymentService().checkPaymentById(order.paymentId())
        );
        var productResult = new ResultContainer<Boolean>();
        Thread thread2 = new Thread(
                () -> productResult.value = getProductService().checkAvailabilityByProductId(order.productId())
        );
        thread1.start();
        thread2.start();
        Address address = getAddressService().obtainAddressByClientId(order.clientId());
        try {
            thread1.join();
            thread2.join();
            if (Boolean.TRUE.equals(paymentResult.value)
                    && Boolean.TRUE.equals(productResult.value)
                    && address != null) {
                return new Delivery(order.clientId(), address);
            } else {
                throw new IllegalStateException();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
