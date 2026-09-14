package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Address;
import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;
import com.github.asmln.multi.serial_parallel.integration.AddressService;
import com.github.asmln.multi.serial_parallel.integration.PaymentService;
import com.github.asmln.multi.serial_parallel.integration.ProductService;

public class SerialDeliveryService extends AbstractDeliveryService {
    @Override
    public String description() {
        return "Последовательные запросы";
    }
    @Override
    public Delivery createDelivery(Order order) {
        boolean paymentOk = getPaymentService().checkPaymentById(order.paymentId());
        boolean productOk = getProductService().checkAvailabilityByProductId(order.productId());
        Address address = getAddressService().obtainAddressByClientId(order.clientId());
        if (paymentOk && productOk && address != null) {
            return new Delivery(order.clientId(), address);
        } else {
            throw new IllegalStateException();
        }
    }
}
