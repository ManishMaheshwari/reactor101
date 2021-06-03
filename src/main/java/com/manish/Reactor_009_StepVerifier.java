package com.manish;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Reactor_009_StepVerifier {
    public static void main(String[] args) {
        Reactor_009_StepVerifier sv = new Reactor_009_StepVerifier();
        sv.normallyCompletingFlux();
        sv.testVerifyThenAssert();
        sv.testTerminatedWithException();
        sv.timeBasedVerifiers();
        sv.moreTimeBasedVerifiers();
        sv.verifyDropped();
    }

    public void normallyCompletingFlux() {
        Flux<String> source = Flux.just("Manish", "Amit", "Tarun");
        StepVerifier.create(source)
                .expectSubscription()
                .expectNext("Manish")
                .expectNext("Amit")
                .expectNextMatches(val -> val.startsWith("T"))
                .expectComplete()
                .verify();
    }

    public void testVerifyThenAssert() {
        Flux<String> source = Flux.just("Manish", "Amit", "Tarun");
        StepVerifier.create(source)
                .expectSubscription()
                .expectNext("Manish")
                .expectNext("Amit")
                .expectNextMatches(val -> val.startsWith("T"))
                .expectComplete()
                .verifyThenAssertThat()
                .hasNotDiscardedElements()
                .hasNotDroppedElements()
                .hasNotDroppedErrors();
    }

    public void testTerminatedWithException() {
        Flux<String> source = Flux.just("Manish", "Amit", "Tarun")
                .concatWith(Mono.error(new RuntimeException("Error here.")));
        StepVerifier.create(source)
                .expectSubscription()
                .expectNextCount(3)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Error here.")
                        && throwable instanceof RuntimeException)
                .verify();
    }

    public void timeBasedVerifiers() {
        StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofHours(3)))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(2))
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    public void moreTimeBasedVerifiers() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(10)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(7))
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0L)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext(1L)
                .expectComplete()
                .verify();
    }

    public void verifyDropped() {
        Flux<Integer> flux = Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sink.next(4);//Must be dropped

        });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1)
                .expectNextCount(2) //2 more items
                .expectComplete()
                .verifyThenAssertThat()
                .hasDropped(4)
                .tookLessThan(Duration.ofMillis(1050));
    }

}
