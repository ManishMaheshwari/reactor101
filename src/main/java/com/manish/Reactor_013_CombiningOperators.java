package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Reactor_013_CombiningOperators {
    public static void main(String[] args) throws InterruptedException {

        Flux.concat(Flux.range(1, 2).delayElements(Duration.ofMillis(500)),
                Flux.just("Manish", "Tarun"))
                .subscribe(System.out::println);

        Flux.concatDelayError(Helper.getErroringFlux(),
                Flux.just("Manish", "Tarun"))
                .subscribe(System.out::println);

        Helper.hold(10);
    }
}
