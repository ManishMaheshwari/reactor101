package com.manish;

import com.manish.util.Helper;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class Reactor_006_SideEffects {
    public static void main(String[] args) throws InterruptedException {
        completesSuccessfully();
        errors();
        cancel();
        Helper.hold(2);
    }

    private static void completesSuccessfully() {
        Helper.divider("Flux that completes successfully");
        Flux.range(100, 5)
                .doFirst(() -> System.out.println("doFirst"))
                .doOnSubscribe(subs -> {
                    System.out.println("doOnSubscribe");
                })
                .doOnRequest(value -> System.out.println("doOnRequest: " + value))//Unbounded
                .doOnNext(value -> System.out.println("doOnNext: " + value))
                .doOnEach(signal -> System.out.println("doOnEach Signal: " + signal.get()))//gets value. But has operations to get state.
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .doOnError(err -> System.out.println("doOnError: " + err))
                .doFinally(signalType -> System.out.println("doFinally signalType: " + signalType.toString()))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);
    }

    private static void errors() {
        Helper.divider("Flux that errors after 3");
        Helper.getErroringFlux()
                .doFirst(() -> System.out.println("doFirst"))
                .doOnSubscribe(subs -> {
                    System.out.println("doOnSubscribe");
                })
                .doOnRequest(value -> System.out.println("doOnRequest: " + value))//Unbounded
                .doOnNext(value -> System.out.println("doOnNext: " + value))
                .doOnEach(signal -> System.out.println("doOnEach Signal: " + signal.get()))//gets value. But has operations to get state.
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .doOnError(err -> System.out.println("doOnError: " + err))
                .doFinally(signalType -> System.out.println("doFinally signalType: " + signalType.toString()))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);
    }

    private static void cancel() {
        Helper.divider("Subscriber cancels at 105");
        Flux.range(100, 50)
                .doFirst(() -> System.out.println("doFirst"))
                .doOnSubscribe(subs -> {
                    System.out.println("doOnSubscribe");
                })
                .doOnRequest(value -> System.out.println("doOnRequest: " + value))//Unbounded
                .doOnNext(value -> System.out.println("doOnNext: " + value))
                .doOnEach(signal -> System.out.println("doOnEach Signal: " + signal.get()))//gets value. But has operations to get state.
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .doOnError(err -> System.out.println("doOnError: " + err))
                .doFinally(signalType -> System.out.println("doFinally signalType: " + signalType.toString()))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        if (value.equals(105)) cancel();
                        request(1);
                    }
                });
    }
}
