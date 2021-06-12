package com.manish.webclient;

import com.manish.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class Web_001 {

    public static final Logger LOGGER = LoggerFactory.getLogger(Web_001.class);

    public static void main(String[] args) throws InterruptedException {

        WebClient wc = WebClient.builder()
                .baseUrl("https://httpbin.org")
                .defaultHeader("Client", "WebClient")
                .build();

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

        Helper.hold(5);
    }
}
