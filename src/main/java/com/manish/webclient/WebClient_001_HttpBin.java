package com.manish.webclient;

import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

/**
This example has webclient connecting to https://httpbin.org
 */
public class WebClient_001_HttpBin {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebClient_001_HttpBin.class);

    public static void main(String[] args) throws InterruptedException {

        WebClient wc = WebClient.builder()
                .baseUrl("https://httpbin.org")
                .defaultHeader("Client", "WebClient")
                .build();

//        basicGet(wc);
//        jsonGet(wc);
        basicPost(wc);

        Helper.hold(5);
    }

    private static void basicGet(WebClient wc) {
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/get")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(data -> LOGGER.info(data));
    }

    private static void jsonGet(WebClient wc) {
        Helper.divider("Gets JSON Data");
        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/json")
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(data -> LOGGER.info(data));
    }

    public static void basicPost(WebClient wc) {
        Helper.divider("Post request");
        wc
                .post()
                .uri(uriBuilder ->
                        uriBuilder.path("/post")
                                .build()
                )
                .bodyValue("HELLO data")
                .retrieve()
//                .onStatus(httpStatus -> httpStatus.is2xxSuccessful(), ClientResponse::createException)
                .bodyToMono(String.class)

                .subscribe(data -> LOGGER.info(data));
    }
}