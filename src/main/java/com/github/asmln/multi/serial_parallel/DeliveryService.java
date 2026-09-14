package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Delivery;
import com.github.asmln.multi.serial_parallel.dto.Order;

public interface DeliveryService {
    String description();
    Delivery createDelivery(Order order);
}
