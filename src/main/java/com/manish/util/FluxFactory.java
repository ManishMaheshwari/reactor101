package com.manish.util;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class FluxFactory {

    public static Flux<Person> getFlux(int count, int delay, boolean endInError) {

//        List<PersonX> persons = new LinkedList<>();
//        for (int i = 0; i < count; i++) {
//            PersonX p = new PersonX(names[i% names.length], i);
//            persons.add(p);
//        }

        Flux<Person> flux = Flux.fromStream(Person.getPersons(count))
                .delayElements(Duration.ofMillis(delay))
                .concatWith(endInError? Flux.error(new RuntimeException("Error encountered in Flux")): Flux.empty());

        return flux;

    }

    static String[] names = {"Manish", "Amit", "Tarun", "Chander", "Hemant"};


    public static void main(String[] args) throws InterruptedException {
//        FluxFactory.getFlux(2, 2000, false)
//                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        FluxFactory.getFlux(10, 100, true)
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);
        Helper.hold(15);

    }

}
