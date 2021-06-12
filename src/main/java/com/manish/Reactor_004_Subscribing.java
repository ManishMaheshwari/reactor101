package com.manish;

import com.manish.util.Helper;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Reactor_004_Subscribing {
    public static void main(String[] args) throws InterruptedException {

        Helper.divider("Data & Error Consumer");
        Helper.getErroringFlux().subscribe(
                System.out::println, //data consumer
                System.err::println, //error consumer. In the absence of errorConsumer, an error in Publisher kills the thread.
                () -> System.out.println("Done with this.")
        );
        Helper.hold(2);

        Helper.divider("Subscription Consumer");
        Flux.range(1, 10)
                .subscribe(
                        System.out::println, //data consumer
                        System.err::println, //error consumer
                        () -> System.out.println("Done with this."),
                        subscriptionConsumer -> {
                            subscriptionConsumer.request(2);  //onSubscribe, if implemented requires the request() operation to be called.
                        }
                );


        Helper.divider("Testing completeRunnable");
        Flux.just("Good", "Better", "Best")
                .subscribe(
                        System.out::println, //data consumer
                        System.err::println, //error consumer
                        () -> System.out.println("Done with this.")
                );

        Helper.divider("Testing Disposable");
        Disposable disposable = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(200))
                .subscribe(System.out::println);
        System.out.println("Before dispose : " + disposable.isDisposed());
        TimeUnit.MILLISECONDS.sleep(2000);
        disposable.dispose();
        System.out.println("After dispose : " + disposable.isDisposed());

        Helper.hold(10);

    }


}
