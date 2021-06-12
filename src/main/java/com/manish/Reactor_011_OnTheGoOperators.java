package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

public class Reactor_011_OnTheGoOperators {
    public static void main(String[] args) {

        Helper.divider("Filter with predicate");
        Flux.range(1, 100)
                .filter(i -> i % 25 == 0)
                .subscribe(Helper.dataConsumer);

        Helper.divider("Map - transform data on the way (blocking)");
        Flux.just("Manish", "Tarun", "Amit")
                .map(data -> data.toUpperCase() + ": " + data.length())
                .subscribe(Helper.dataConsumer);
    }
}
