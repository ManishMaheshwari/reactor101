package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Reactor_013_CombiningOperators {
    public static void main(String[] args) throws InterruptedException {

        Helper.divider("concat");
        Flux.concat(Flux.range(1, 2).delayElements(Duration.ofMillis(500)),
                Flux.just("Manish", "Tarun"))
                .subscribe(System.out::println, System.out::println);
        Helper.hold(2);

        Helper.divider("concat - note the lazy subscription of second publisher");
        Flux.concat(Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(500)),
                Flux.just(10, 20, 30, 40).delayElements(Duration.ofMillis(500)))
                .subscribe(System.out::println, System.out::println);
        Helper.hold(5);

        Helper.divider("concatDelayError");
        Flux.concatDelayError(Helper.getErroringFlux(),
                Flux.just("Manish", "Tarun"))
                .subscribe(System.out::println, System.out::println);

        Helper.divider("Merge - All merge variations subscribe eagerly to the list of Publishers");
        Flux.merge(Flux.range(500, 3).delayElements(Duration.ofMillis(500)),
                Flux.range(-500, 3).delayElements(Duration.ofMillis(300)))
                .subscribe(System.out::println, System.out::println);
        Helper.hold(2);

        Helper.divider("Merge - another way");
        Flux.range(100, 3)
                .delayElements(Duration.ofMillis(500))
                .mergeWith(Flux.range(-100, 3).delayElements(Duration.ofMillis(300)))
                .subscribe(System.out::println, System.out::println);
        Helper.hold(2);

        Helper.divider("Merge Sequential - note the eager subscription of all Publishers");
        Flux.mergeSequential(Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(500)),
                Flux.just(10, 20, 30, 40).delayElements(Duration.ofMillis(500)))
                .subscribe(System.out::println, System.out::println);
        Helper.hold(3);

        Helper.divider("Merge Ordered - keeps last value from each source & emits smaller of them");
        Flux.mergeOrdered(Flux.just(1, 21, 3, 4).delayElements(Duration.ofMillis(500)),
                Flux.just(10, 20, 30, 40).delayElements(Duration.ofMillis(300)))
                .subscribe(System.out::println, System.out::println);
        Helper.hold(5);


        Helper.divider("Zip - combines one item from each Publisher using a combinator function");
        Flux.zip(Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(400)),
                Flux.just(10, 20, 30, 40).delayElements(Duration.ofMillis(300)),
                (a,b) -> a+b)
                .subscribe(System.out::println, System.out::println);
        Helper.hold(2);

        Helper.divider("CombineLatest - combines the latest item from first Publisher with each item of second Publisher");
        Flux.combineLatest(Flux.just(400, 800, 1200, 1600).delayElements(Duration.ofMillis(400)),
                Flux.just(300, 600, 900, 1200).delayElements(Duration.ofMillis(300)),
                (a,b) -> "Combining " + a + " with " + b)
                .subscribe(System.out::println, System.out::println);
        Helper.hold(2);



        Helper.hold(10);
    }
}
