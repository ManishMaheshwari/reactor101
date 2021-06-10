package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Supplier;

public class Reactor_016_ReactiveToBlocking {
    public static void main(String[] args) throws InterruptedException {

        Helper.divider("BlockFirst & BlockLast");
        System.out.println(Flux.range(1, 5)
                .blockFirst());

        System.out.println(Flux.range(1, 5)
                .blockLast());

        Helper.divider("Turn Blocking --->> reactive");
        Flux.fromIterable(Arrays.asList("Manish", "Amit", "Chander"))
                .subscribe(Helper.dataConsumer);

        Helper.divider("Take a blocking supplier, and call it when the Flux is subscribed");
        Supplier<Integer> blockingSupplier = () -> 100;

        Flux.defer(() -> Flux.just("Manish", "Amit", "Chander").delayElements(Duration.ofMillis(500)))
                .subscribe(Helper.dataConsumer);

        Helper.hold(3);
    }
}
