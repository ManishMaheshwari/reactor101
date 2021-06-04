package com.manish;

import com.manish.util.Helper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class Reactor_015_Transform {
    public static void main(String[] args) {
        Helper.divider("Transform - Flux");
        Function<Flux<String>, Flux<String>> fluxTransform =
                flux -> flux.filter(color -> !color.equalsIgnoreCase("red"))
                .map(color -> color.toUpperCase());


        Flux.just("red", "orange", "Red", "green", "blue")
                .transform(fluxTransform)
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);


        Helper.divider("Transform - Mono");
        Function<Mono<String>, Mono<Integer>> monoTransform =
                mono -> mono.map(val -> val.length());

        Mono.just("faghdfhjgeukgtfujgfhjsgfjgew")
                .transform(monoTransform)
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);
    }
}
