package com.manish;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

public class Reactor_005_BaseSubscriber {
    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 30)
                .subscribe(new SampleSubscription<>());
    }
}

class SampleSubscription<T> extends BaseSubscriber<T> {
    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Just subscribed.");

        //Controlling backpressure
        request(1);
    }

    @Override
    protected void hookOnNext(T value) {
        System.out.println(value);
        if (value.equals(6)) {
            request(2);
        } else if (value.equals(8)) {
            request(3);
        } else if (value.equals(15)) {
            cancel();
        } else {
            request(1);
        }
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        System.out.println("Stream errored out: " + throwable.getMessage());
    }

    @Override
    protected void hookOnComplete() {
        System.out.println("Stream is complete.");
    }

    @Override
    protected void hookOnCancel() {
        System.out.println("Subscription is cancelled.");
    }

    @Override
    protected void hookFinally(SignalType type) {
        System.out.println("Stream finally hook called");
    }
}
