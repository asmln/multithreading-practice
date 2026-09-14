package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;
import com.github.asmln.multi.serial_parallel.integration.AddressService;
import com.github.asmln.multi.serial_parallel.integration.PaymentService;
import com.github.asmln.multi.serial_parallel.integration.ProductService;

public abstract class AbstractDeliveryService implements DeliveryService {

    private final PaymentService paymentService = new PaymentService();
    private final ProductService productService = new ProductService();
    private final AddressService addressService = new AddressService();

    protected PaymentService getPaymentService() {
        return paymentService;
    }

    protected ProductService getProductService() {
        return productService;
    }

    protected AddressService getAddressService() {
        return addressService;
    }
}
