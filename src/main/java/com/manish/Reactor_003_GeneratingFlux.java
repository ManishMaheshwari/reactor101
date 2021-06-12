package com.manish;

import com.manish.tcp.Tcp_01_Server_Basic;
import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.LongConsumer;

public class Reactor_003_GeneratingFlux {

    public static final Logger LOGGER = LoggerFactory.getLogger(Reactor_003_GeneratingFlux.class);

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

        Helper.divider("Flux that emits & completes & drops an element - uses Async FluxSink.\n Also has overflow strategy to deal with cancels");
        Flux.<Integer>create(sink -> {
            sink.onRequest(x -> LOGGER.info("onRequest: {}",x));
            sink.next(1);
            long requested = sink.requestedFromDownstream();
            LOGGER.info("Outstanding requested values: {}", requested);
            sink.next(2);
            sink.next(3);
            sink.next(4);
            sink.complete();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sink.next(5);//Must be dropped

        }).
                log("DroppingFlux")
                .subscribe(Helper.dataConsumer, Helper.errorConsumer,
                () -> System.out.println("Completed."),
                subscription -> subscription.request(3));

        Helper.hold(5);

    }


}
