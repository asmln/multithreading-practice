package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Address;
import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualFutureDeliveryService extends AbstractDeliveryService {
    @Override
    public String description() {
        return "Virtual Threads и Future";
    }
    @Override
    public Delivery createDelivery(Order order) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<Boolean> paymentFuture = executor.submit(
                    () -> getPaymentService().checkPaymentById(order.paymentId())
            );
            Future<Boolean> productFuture = executor.submit(
                    () -> getProductService().checkAvailabilityByProductId(order.productId())
            );
            Future<Address> addressFuture = executor.submit(
                    () -> getAddressService().obtainAddressByClientId(order.clientId())
            );
            Boolean paymentOk = paymentFuture.get();
            Boolean productOk = productFuture.get();
            Address address = addressFuture.get();
            if (paymentOk && productOk && address != null) {
                return new Delivery(order.clientId(), address);
            } else {
                throw new IllegalStateException();
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
