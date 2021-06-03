package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Mono;

public class Reactor_002_MonoBasics {
    public static void main(String[] args) throws InterruptedException {

        //Creating Mono
        Helper.divider("Empty Mono");
        Mono<String> empty = Mono.empty();
        empty.subscribe(Helper.dataConsumer);

        Helper.divider("Mono with its items");
        Mono.just("Manish")
                .subscribe(Helper.dataConsumer);

    }
}
