package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class Reactor_010_TestPublisher {
    public static void main(String[] args) {
        Reactor_010_TestPublisher testPublisher = new Reactor_010_TestPublisher();
        testPublisher.elementsAndError();
        testPublisher.usageInUnitTesting();
    }

    public void elementsAndError() {
        Helper.divider("Using for learning");
        Flux<Object> flux = TestPublisher.createCold()
                .emit("Tarun", "Manish", "Amit")
                .error(new RuntimeException("Oops"))
                .flux();

        //Printing
        flux.subscribe(Helper.dataConsumer, Helper.errorConsumer);

        //Using with Step Verifier
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("Tarun", "Manish", "Amit")
                .expectError()
                .verify();
    }

    public void usageInUnitTesting() {
        Helper.divider("Using for Unit Testing");
        TestPublisher<String> testPublisher = TestPublisher.create();

        UppercaseConverter uppercaseConverter = new UppercaseConverter(testPublisher.flux());

        StepVerifier.create(uppercaseConverter.getUpperCase())
                .then(() -> testPublisher.emit("Tarun", "Manish", "Amit"))
                .expectNext("TARUN", "MANISH", "AMIT")
                .expectComplete()
                .verify();
    }
}


class UppercaseConverter {
    private final Flux<String> source;

    UppercaseConverter(Flux<String> source) {
        this.source = source;
    }

    Flux<String> getUpperCase() {
        return source
                .map(String::toUpperCase);
    }
}
