package com.manish.webclient;

import com.manish.util.Helper;
import com.manish.util.Person;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.CoreSubscriber;

import java.util.concurrent.TimeUnit;

public class WebClient_005_BackpressureViaTcpFlowControl {


    /**
     * Run this with tcpdump ON.
     *
     * We request insane number of Flux data from remote.
     * However, we exert backpressure from time to time, when we are reading 20th and every 4000th data.
     *
     * Backpressure is applied using TCP Flow Control mechanisms.
     * The emptying of input byte buffer is slowed down, resulting on non-Acking at TCP layer to the remote.
     * Remote thus slows down.
     *
     * Run com.manish.httpserver.spring.Application server prior to testing webclient
     * To view tcpdump on OSX when running this:
     * sudo tcpdump -vvXn -i lo0 port 8080 -X
     *
     */

    public static final Logger LOGGER = LoggerFactory.getLogger(WebClient_005_BackpressureViaTcpFlowControl.class);

    public static String HOST_URL = "http://localhost:8080";

    public static void main(String[] args) throws InterruptedException {

        WebClient wc = WebClient.builder()
                .baseUrl(HOST_URL)
                .defaultHeader("Client", "WebClient")
                .build();

        int count = 200000;

        Helper.divider("Expect TCP Flow Control to exert backpressure on remote");

        wc
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .pathSegment("personstream", count + "")
                                .queryParam("param1", "A")
                                .queryParam("param2", "B")
                                .build()
                )
                .retrieve()
                .bodyToFlux(Person.class)

                .subscribe(new CoreSubscriber<Person>() {
                    Subscription s;
                    @Override
                    public void onSubscribe(Subscription s) {
                        LOGGER.info("Subscription begun");
                        this.s = s;
                        s.request(20);
                    }

                    @Override
                    public void onNext(Person person) {
                        LOGGER.info("on Next recd: {}", person);
                        if(person.getId() == 20 ){
                            try {
                                TimeUnit.SECONDS.sleep(30);
                                s.request(4000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        if(person.getId()% 4000 == 0){
                            try {
                                TimeUnit.SECONDS.sleep(30);
                                s.request(4000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LOGGER.info("onError: {}", t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LOGGER.info("onComplete triggered");
                    }
                });

        Helper.hold(1000000000);
        return;


    }

    public static void fluxOfPersonsWithDelay(WebClient wc) throws InterruptedException {

    }

}
