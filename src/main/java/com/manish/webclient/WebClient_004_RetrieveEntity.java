package com.manish.webclient;

import com.manish.util.Helper;
import com.manish.util.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebClient_004_RetrieveEntity {


    /**
     * Run com.manish.httpserver.spring.Application server prior to testing webclient
     * To view tcpdump on OSX when running this:
     * sudo tcpdump -vvXn -i lo0 port 8080 -X
     */

    public static final Logger LOGGER = LoggerFactory.getLogger(WebClient_004_RetrieveEntity.class);

    public static String HOST_URL = "http://localhost:8080";

    public static void main(String[] args) throws InterruptedException {

        WebClient wc = WebClient.builder()
                .baseUrl(HOST_URL)
                .defaultHeader("Client", "WebClient")
                .build();

        fluxOfPersons(wc);
        return;


    }

    /**
     * Using toEntityFlux - gets headers, status, body, etc
     */
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
                .toEntityFlux(Person.class)
                .subscribe(data -> {
                    LOGGER.info("Data recd: {}", data.getStatusCode());
                    for (Map.Entry<String, List<String>> entry: data.getHeaders().entrySet()) {
                        LOGGER.info("Header entry: {}: {}", entry.getKey(), entry.getValue());
                    }
                  data.getBody().subscribe(person -> LOGGER.info("Body recd - {}", person));
                });
        Helper.hold(10);
    }



}
