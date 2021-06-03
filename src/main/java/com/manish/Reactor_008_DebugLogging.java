package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;

public class Reactor_008_DebugLogging {


    public static void main(String[] args) {

        /**
         * TODO: Try various log operations at different levels in operator chain.
         */
        Flux.range(1, 100)
                .log()
                .filter(i -> i % 10 == 0)
                .subscribe(Helper.dataConsumer);
    }
}
