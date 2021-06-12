package com.manish.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class MonoFactory {

    public static final Logger LOGGER = LoggerFactory.getLogger(MonoFactory.class);

    public static Mono<Person> getMono(int delay, boolean error) {

        if (error) {
            return Mono.error(new RuntimeException("Mono encountered an error"));
        }

        Person person = new Person(1, "Manish", "Maheshwari", 20);
        Gson gson = new Gson();
        String json = gson.toJson(person);
        LOGGER.info("Person json {}", json);

        Mono<Person> mono = Mono.just(person)
                .delayElement(Duration.ofMillis(delay));

        return mono;
    }

    public static void main(String[] args) throws InterruptedException {

        MonoFactory.getMono(5000, false)
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        MonoFactory.getMono(0, false)
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        MonoFactory.getMono(8000, true)
                .subscribe(Helper.dataConsumer, Helper.errorConsumer);

        Helper.hold(15);
    }

}
