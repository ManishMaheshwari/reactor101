package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Reactor_003_GeneratingFlux {
    public static void main(String[] args) throws InterruptedException {

        Helper.divider("Table of 3 - uses SynchronousSink");
        Flux.generate(
                () -> 100, //initialState supplier
                (state, sink) -> {
                    sink.next(state + " x 3 = " + state * 3);
                    if (state == 90) sink.complete();
                    if (state == 80) sink.error(new RuntimeException("Must never reach here"));
                    return state - 1; //return state to use for next data item
                }
        ).subscribe(Helper.dataConsumer);

        Helper.divider("Flux of Friends");
        List list = Arrays.asList("Amit", "Tarun", "Chandar");
        Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next(list.get(state));
                    if (state == 2) sink.complete();
                    return state + 1;
                }
        ).subscribe(Helper.dataConsumer);

        Helper.divider("Flux that emits & completes & drops an element - uses Async FluxSink");
        Flux.<Integer>create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sink.next(4);//Must be dropped

        }).subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.hold(5);

    }


}
