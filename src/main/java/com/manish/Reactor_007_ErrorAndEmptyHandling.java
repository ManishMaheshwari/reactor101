package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Reactor_007_ErrorAndEmptyHandling {

    public static void main(String[] args) throws InterruptedException {

        Helper.divider("onErrorResume");
        Helper.getErroringFlux()
                .onErrorResume(throwable -> Flux.range(-100, 5))
                .subscribe(Helper.dataConsumer);

        Helper.divider("onErrorReturn");
        Helper.getErroringFlux()
                .onErrorReturn(-100)
                .subscribe(Helper.dataConsumer);

        Helper.divider("onErrorMap");
        Helper.getErroringFlux()
                .onErrorMap(throwable -> new RuntimeException("Sorry!"))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.divider("SwitchIfEmpty - Fallback upon empty Publisher");
        Mono.empty().switchIfEmpty(Mono.just("One"))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Flux.empty().switchIfEmpty(Mono.just("MonoOne"))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

    }
}
