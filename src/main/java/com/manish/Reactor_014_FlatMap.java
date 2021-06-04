package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;

public class Reactor_014_FlatMap {
    public static void main(String[] args) throws InterruptedException {
        Helper.divider("Flat Map - Starts a new Flux Publisher on the way");
        Flux.just("Manish, Tarun, Amit", "Chandar, Ashutosh")
                .log("main-flux")
                .flatMap(val -> Flux.fromArray(val.split(",")))
                .log("post-flat-map")
                .map(val -> val.trim())
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.hold(2);

        Helper.divider("FlatMap - with parallel schedulers");
        Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5))
                .flatMap(a -> Mono.just(a).subscribeOn(Schedulers.parallel()))
                .doOnNext(val -> System.out.printf("[First Flat Map's out events] Recd %d on thread %s%n", val, Thread.currentThread().getName()))
                .flatMap(
                        val -> {
                            System.out.printf("[Input to second Flat Map] Recd %d in flatMap for further flattening, on thread %s%n", val, Thread.currentThread().getName());
                            val++;
                            return Mono.just(val).subscribeOn(Schedulers.elastic());
                        })
                .subscribe(val -> System.out.printf("[Final Subscriber] Recd %d at subscriber, on thread %s%n", val, Thread.currentThread().getName()));

        Helper.hold(10);

    }
}
