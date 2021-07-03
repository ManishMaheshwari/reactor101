package com.manish.webclient;

import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Run com.manish.httpserver.spring.Application server prior to testing webclient
 */
public class WebClient_002 {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebClient_002.class);

    public static void main(String[] args) throws InterruptedException {

        StopWatch sw = new StopWatch();

        WebClient wc = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("Client", "WebClient")
                .build();

//        expectMonoWithDelay(sw, wc);

        expectFluxWithDelay(sw, wc);
    }

    public static void expectMonoWithDelay(StopWatch sw, WebClient wc) throws InterruptedException {
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
    }

    public static void expectFluxWithDelay(StopWatch sw, WebClient wc) throws InterruptedException {
        Helper.divider("Expect Flux with delay");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/flux/5/1000")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .accept(MediaType.TEXT_EVENT_STREAM) //Advertises that I can accept a stream. Therefore, server sends a Transfer-Encoding: Chunked stream.
                .retrieve()
                .bodyToFlux(String.class)
                .doOnSubscribe(subscription -> sw.start())
                .doOnNext(data -> {
                    sw.stop();
                    sw.start();
                })
                .subscribe(data -> LOGGER.info("Data recd: {}, \nMeasured delay: {} ms", data, sw.getLastTaskTimeMillis()))
        ;
        Helper.hold(10);
    }
}
