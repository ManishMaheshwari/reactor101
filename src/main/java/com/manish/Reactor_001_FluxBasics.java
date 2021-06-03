package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class Reactor_001_FluxBasics {
    public static void main(String[] args) throws InterruptedException {

        //Creating Flux from just.
        Helper.divider("Creating Flux from just - Stream subscribed and triggered but thers's no dataConsumer");
        Flux<String> someFruits = Flux.just("Apple", "Orange", "Mango");
        someFruits.subscribe();

        //Creating Flux from just
        Helper.divider("Creating Flux from just");
        Flux<String> fruits = Flux.just("Apple", "Orange", "Mango");
        fruits.subscribe(str -> System.out.println(str));

        //Creating Flux from Iterables
        Helper.divider("Creating Flux from Iterables");
        Flux<String> friends = Flux.fromIterable(Arrays.asList("Amit", "Tarun"));
        friends.subscribe(System.out::println);

        //Creating Flux from Arrays
        Helper.divider("Creating Flux from Arrays");
        String[] arr = {"A", "B", "C"};
        Flux.fromArray(arr)
                .subscribe(System.out::println);

        //Range
        Helper.divider("Range");
        Flux.range(10, 3)
                .subscribe(System.out::println);

        //Delay Elements
        Helper.divider("Delay Elements");
        Flux.range(1, 5)
                .delayElements(Duration.ofMillis(600))
                .subscribe(System.out::println);

        Flux.just("Manish", "Tarun", "Chandar")
                .delayElements(Duration.ofMillis(800))
                .subscribe(System.out::println);

        Helper.hold(4);

        Helper.divider("Flux Interval");
        Flux.interval(Duration.ofSeconds(1)).take(5)
                .subscribe(Helper.dataConsumer);

        Helper.hold(10);
    }
}
