package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

public class Reactor_007_ErrorHandling {

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

    }
}
