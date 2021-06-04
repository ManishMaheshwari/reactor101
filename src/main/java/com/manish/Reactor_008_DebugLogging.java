package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;


public class Reactor_008_DebugLogging {

    public static void main(String[] args) {

        Helper.divider("Basic Logging - Requires SLF4J & a provider on classpath");
        Flux.range(1, 100)
                .log("range")
                .filter(i -> i % 10 == 0)
                .log("filter")
                .subscribe(Helper.dataConsumer);

//        Helper.divider("Advanced Logging");
//        Flux.range(1, 100)
//                .filter(i -> i % 10 == 0)
//                .log("some" , Level.ALL,  SignalType.ON_NEXT, SignalType.ON_COMPLETE)
//                .subscribe(Helper.dataConsumer);
    }
}
