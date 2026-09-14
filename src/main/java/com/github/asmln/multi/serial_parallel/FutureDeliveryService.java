package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Address;
import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;

import java.util.concurrent.*;

public class FutureDeliveryService extends AbstractDeliveryService {
    @Override
    public String description() {
        return "Future";
    }
    @Override
    public Delivery createDelivery(Order order) {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            Future<Boolean> paymentFuture = executor.submit(
                    () -> getPaymentService().checkPaymentById(order.paymentId())
            );
            Future<Boolean> productFuture = executor.submit(
                    () -> getProductService().checkAvailabilityByProductId(order.productId())
            );
            Address address = getAddressService().obtainAddressByClientId(order.clientId());
            Boolean paymentOk = paymentFuture.get();
            Boolean productOk = productFuture.get();
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
