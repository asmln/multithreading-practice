package com.github.asmln.multi.serial_parallel;

import com.github.asmln.multi.serial_parallel.dto.Order;

import java.util.List;
import java.util.Locale;

public class Main {
    static List<DeliveryService> deliveryServices = List.of(
            new SerialDeliveryService(),
            new ThreadsDeliveryService(),
            new FutureDeliveryService(),
            new CompletableFutureDeliveryService(),
            new VirtualFutureDeliveryService()
    );
    static void main() {
        IO.println("\uD83D\uDCA1Запросы в несколько медленных сервисов");
        Order order = new Order(1L, 222L, 333L);
        for (var deliveryService: deliveryServices) {
            long start = System.currentTimeMillis();
            deliveryService.createDelivery(order);
            IO.println(
                    String.format(
                        Locale.FRANCE,
                        "%s: время оформления = %,d мсек.",
                        deliveryService.description(),
                        (System.currentTimeMillis() - start)
                    )
            );
        }
    }
}
