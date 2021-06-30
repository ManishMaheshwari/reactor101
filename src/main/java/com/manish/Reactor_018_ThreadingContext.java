package com.manish;

import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;

public class Reactor_018_ThreadingContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reactor_018_ThreadingContext.class);

    public static void main(String[] args) throws InterruptedException {

        Helper.divider("By default, all events happen in the subscriber's thread (main)");
        Flux.fromIterable(Arrays.asList("Amit", "Tarun", "Chandar"))
                .doOnNext(s -> LOGGER.info("doOnNext: [{}] {}", Thread.currentThread().getName(), s))
                .subscribe(s -> LOGGER.info("dataConsumer: [{}] {}", Thread.currentThread().getName(), s));

        Helper.hold(3);

        Helper.divider("All events are executing on main thread, and getting blocked");
        Flux.fromIterable(Arrays.asList("google.com", "bing.com", "yahoo.com"))
                .map(url -> Helper.blockingClient(url))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));

        Flux.fromIterable(Arrays.asList("a.com", "b.com", "c.com"))
                .map(url -> Helper.blockingClient(url))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));
        Helper.hold(2);

        Helper.divider("publishOn boundedElastic - To choose a new thread (from a pool) for publishing events");
        Flux.fromIterable(Arrays.asList("googleElastic.com", "bingElastic.com", "yahooElastic.com"))
                .publishOn(Schedulers.boundedElastic())
                .map(url -> Helper.blockingClient(url))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));

        Flux.fromIterable(Arrays.asList("aElastic.com", "bElastic.com", "cElastic.com"))
                .publishOn(Schedulers.boundedElastic())
                .map(url -> Helper.blockingClient(url))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));

        Helper.hold(10);

        Helper.divider("subscribeOn boundedElastic - Subscribers dictating the thread (from a pool) to use");
        Flux.fromIterable(Arrays.asList("googleElastic.com", "bingElastic.com", "yahooElastic.com"))
                .subscribeOn(Schedulers.boundedElastic())
                .map(url -> Helper.blockingClient(url))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));

        Flux.fromIterable(Arrays.asList("aElastic.com", "bElastic.com", "cElastic.com"))
                .subscribeOn(Schedulers.boundedElastic())
                .map(url -> Helper.blockingClient(url))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));

        Helper.hold(10);

        Helper.divider("Use more concurrency by flatMap and inner publisher");
        Flux.range(1, 20)
                .map(i -> "slow" + i + ".com")
                .flatMap(url ->
                        //wrap the blocking call in a Mono & subscribe (or publish) to it in boundedElastic
                        Mono.fromCallable(() -> Helper.blockingClient(url))
                                .subscribeOn(Schedulers.boundedElastic())
                )
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));

        Helper.hold(5);

        Helper.divider("Using publishOn as well as subscribeOn");
        Flux.range(1, 5)
                .publishOn(Schedulers.newBoundedElastic(5, 5, "pubThread"))
                .map(i -> "pubsub" + i + ".com")
                .map(url -> Helper.blockingClient(url))
                .subscribeOn(Schedulers.newBoundedElastic(5, 5, "subThread"))
                .subscribe(body -> System.out.format("[%s] Content of url - %s%n",
                        Thread.currentThread().getName(), body));
        Helper.hold(12);
        Schedulers.shutdownNow();
    }
}


