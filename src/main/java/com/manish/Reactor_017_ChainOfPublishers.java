package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Reactor_017_ChainOfPublishers {

    public static void main(String[] args) throws InterruptedException {
        Helper.divider("First - returns the fastest Publisher amongst competing Publishers");
        Flux.firstWithValue(Flux.range(1, 5).delayElements(Duration.ofMillis(1000)),
                Flux.range(100, 5).delayElements(Duration.ofMillis(900)),
                Flux.range(200, 5).delayElements(Duration.ofMillis(500)),
                Flux.range(300, 5).delayElements(Duration.ofMillis(1200)),
                Flux.range(400, 5).delayElements(Duration.ofMillis(600)))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.hold(3);

        Helper.divider("Null aware Mono");
        String user = null;
        (user == null ? Mono.empty() : Mono.just(user))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.divider("then - Returns the provided Publisher when the original Publisher is finally done.");
        /*
        ignore element from this Flux and transform its completion signal into the emission and completion signal of a provided Mono<V>.
        Error signal is replayed in the resulting Mono<V>.
         */
        Flux.range(100, 3)
                .delayElements(Duration.ofMillis(500))
                .then(Mono.just("Manish"))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.hold(3);

        /*
        Return a Mono<Void> that completes when this Flux completes.
        This will actively ignore the sequence and only replay completion or error signals.
         */
        Flux.range(100, 3)
                .delayElements(Duration.ofMillis(200))
                .then()
                .subscribe(Helper.dataConsumer, Helper.errorConsumer, () -> System.out.println("runnable complete"));

        Helper.hold(2);


    }
}
