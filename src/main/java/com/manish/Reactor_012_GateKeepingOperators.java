package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

public class Reactor_012_GateKeepingOperators {
    public static void main(String[] args) {

        Helper.divider("Count");
        Flux.range(1, 10)
                .count()
                .subscribe(Helper.dataConsumer);

        Helper.divider("Distinct");
        Flux.just("Good", "Better", "Good", "Best", "Good", "Best")
                .distinct()
                .subscribe(Helper.dataConsumer);

        Helper.divider("Buffer as list");
        Flux.range(1, 10)
                .buffer(3)
                .subscribe(Helper.dataConsumer);

        Helper.divider("Sort");
        Flux.just("Good", "Better", "Good", "Best", "Good", "Best")
                .sort()
                .subscribe(Helper.dataConsumer);

        Helper.divider("Repeat");
        Flux.just("Good", "Better", "Best")
                .repeat(1)
                .subscribe(Helper.dataConsumer);
    }
}
