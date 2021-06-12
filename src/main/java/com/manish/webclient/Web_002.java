package com.manish.webclient;

import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;

public class Web_002 {

    public static final Logger LOGGER = LoggerFactory.getLogger(Web_002.class);

    public static void main(String[] args) throws InterruptedException {

        StopWatch sw = new StopWatch();

        WebClient wc = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("Client", "WebClient")
                .build();

        Helper.divider("Expect Mono with delay");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/mono/manish/5000")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> sw.start())
                .doOnNext(data -> sw.stop())
                .subscribe(data -> LOGGER.info("Data recd: {}, \nMeasured delay: {} ms", data, sw.getTotalTimeMillis()));

        Helper.hold(7);

        Helper.divider("Expect Flux with delay");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/flux/5/1000")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
//                .contentType(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> sw.start())
                .doOnNext(data -> sw.stop())
                .subscribe(data -> LOGGER.info("Data recd: {}, \nMeasured delay: {} ms", data, sw.getTotalTimeMillis()))
        ;
        Helper.hold(10);
    }
}
