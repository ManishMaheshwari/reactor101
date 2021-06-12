package com.manish.webclient;

import com.manish.util.Person;
import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClient_001_Person {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebClient_001_Person.class);

    public static void main(String[] args) throws InterruptedException {

        WebClient wc = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("Client", "WebClient")
                .build();

//        fluxOfPersons(wc);
//        fluxOfPersonsStream(wc);
//        fluxOfPersonsWithDelay(wc);
        monoPerson(wc);
        return;


    }

    public static void fluxOfPersons(WebClient wc) throws InterruptedException {
        Helper.divider("Expect Flux of Persons");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/person/5")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToFlux(Person.class)
                .subscribe(data -> LOGGER.info("Data recd: {}", data));
        Helper.hold(10);
    }

    public static void fluxOfPersonsStream(WebClient wc) throws InterruptedException {
        Helper.divider("Expect Flux of Persons, content type stream");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/personstream/7")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToFlux(Person.class)
                .subscribe(data -> LOGGER.info("Data recd: {}", data));

        Helper.hold(10);

    }

    public static void fluxOfPersonsWithDelay(WebClient wc) throws InterruptedException {
        Helper.divider("Expect Flux of Persons with Delay, content type stream");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/person/15/1000")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToFlux(Person.class)
                .subscribe(data -> LOGGER.info("Data recd: {}", data));

        Helper.hold(20);
    }

    public static void monoPerson(WebClient wc) throws InterruptedException {
        Helper.divider("Expect Mono of Person");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/personmono")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToMono(Person.class)
                .subscribe(data -> LOGGER.info("Data recd: {}", data));
        Helper.hold(2);
    }

}
