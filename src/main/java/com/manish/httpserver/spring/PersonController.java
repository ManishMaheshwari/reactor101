package com.manish.httpserver.spring;

import com.manish.util.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/person/{count}")
    public Flux<Person> findPersonsJson(@PathVariable int count) {
        LOGGER.info("Handling flux - findPersons with count {}", count);
        return Flux.fromStream(Person.getPersons(count))
                .doOnNext(person -> LOGGER.info("Server streams: {}", person));
    }

    /*
    Defining Content Type allows the server and the client to understand the content boundaries,
    so that streaming of data can be done by the server (piecemeal sending), and
    consumption of data can be done by the client correctly.
    Chunked Encoding?? Burp Suite?
     */
    @GetMapping(value = "/personstream/{count}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Person> findPersonsStream(@PathVariable int count) {
        return Flux.fromStream(Person.getPersons(count))
                .doOnNext(person -> LOGGER.info("Server streams: {}", person));
    }

    @GetMapping(value = "/person/{count}/{delay}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Person> findPersonsJsonDelayed(@PathVariable int count, @PathVariable int delay) {
        LOGGER.info("Handling flux - findPersons with count {} and delay {} ms", count, delay);
        return Flux.fromStream(Person.getPersons(count))
                .delayElements(Duration.ofMillis(delay))
                .doOnNext(person -> LOGGER.info("Server streams: {}", person));
    }

    @GetMapping(value = "/personmono", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Person> finfPersonMono() {
        LOGGER.info("Handling mono - finfPersonMono");
        return Mono.just(new Person(1, "Manish", "Maheshwari", 20))
                .doOnNext(person -> LOGGER.info("Server returns a mono: {}", person));
    }


}
