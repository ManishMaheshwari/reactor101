package com.manish;

import com.manish.tcp.Tcp_01_Server_Basic;
import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;

public class Reactor_014_FlatMap {

    public static final Logger LOGGER = LoggerFactory.getLogger(Reactor_014_FlatMap.class);

    public static void main(String[] args) throws InterruptedException {
        Helper.divider("Flat Map - Starts a new Flux Publisher on the way");
        Flux.just("Manish, Tarun, Amit", "Chandar, Ashutosh")
                .log("main-flux")
                .flatMap(val -> Flux.fromArray(val.split(","))) //creates several inner publishers, one from each data item, and merges these publishers.
                .log("post-flat-map")
                .map(val -> val.trim())
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.hold(2);

        Helper.divider("FlatMap - with parallel schedulers");
        Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5))
                .flatMap(a -> Mono.just(a).subscribeOn(Schedulers.parallel()))
                .doOnNext(val -> LOGGER.info("[First Flat Map's out events] Recd {} on thread {}", val, Thread.currentThread().getName()))
                .flatMap(
                        val -> {
                            LOGGER.info("[Input to second Flat Map] Recd {} in flatMap for further flattening, on thread {}", val, Thread.currentThread().getName());
                            val++;
                            return Mono.just(val).subscribeOn(Schedulers.boundedElastic());
                        })
                .subscribe(val -> LOGGER.info("[Final Subscriber] Recd {} at subscriber, on thread {}", val, Thread.currentThread().getName()));

        Helper.hold(10);

    }
}
