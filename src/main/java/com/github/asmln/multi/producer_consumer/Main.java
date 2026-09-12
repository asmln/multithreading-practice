package com.github.asmln.multi.producer_consumer;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static final int MARKET_SIZE = 5;

    static List<Market> MARKETS = List.of(
            new BadMarket(MARKET_SIZE),
            new WaitNotifyMarket(MARKET_SIZE)
    );

    static void main() {
        int stepCount = 100_000;
        // Работа в одном потоке:
        IO.println("➡\uFE0F Однопоточная работа:");
        BadMarket badMarket = new BadMarket(MARKET_SIZE);
        for (int i = 0; i < stepCount; i++) {
            badMarket.put(1);
            badMarket.get();
        }
        IO.println("✅ Работа в одном потоке закончена успешно.");
        // Многопоточка:
        IO.println("\uD83D\uDD00 Многопоточная работа:");
        for (var market: MARKETS) {
            AtomicInteger sentCounter = new AtomicInteger(0);
            AtomicInteger receivedCounter = new AtomicInteger(0);
            try (var executor = Executors.newFixedThreadPool(2)) {
                runProducerConsumer(executor, stepCount, sentCounter, receivedCounter, market);
                executor.shutdown();
                boolean ok = executor.awaitTermination(2, TimeUnit.SECONDS);
                if (!ok) {
                    IO.println(market.description() + ": ❌ Работа была прервана по истечению таймаута.");
                    executor.shutdownNow();
                } else {
                    IO.println(String.format(
                                Locale.FRANCE,
                                market.description() + ": ✅ Обменник закончил работу. Отправлено: %,d, получено %,d.",
                                sentCounter.get(),
                                receivedCounter.get()
                            )
                    );
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                IO.println(market.description() + ": ❌ В процессе работы произошло исключение " + e);
            }
        }
    }

    static void runProducerConsumer(
            ExecutorService executor,
            int stepCount,
            AtomicInteger sentCounter,
            AtomicInteger receivedCounter,
            Market market
    ) {
        executor.submit(() -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < stepCount; i++) {
                market.put(random.nextInt(1, 11));
                sentCounter.incrementAndGet();
            }
        });
        executor.submit(() -> {
            for (int i = 0; i < stepCount; i++) {
                market.get();
                receivedCounter.incrementAndGet();
            }
        });
    }
}
