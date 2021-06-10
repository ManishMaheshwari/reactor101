package com.manish.http;

import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServer;

import java.util.concurrent.TimeUnit;

public class TestHttpServer {

    public static void main(String[] args) throws InterruptedException {
        HttpServer.create()
                .host("0.0.0.0")
                .port(9898)
                .handle((req, res) -> res.sendString(Flux.just("<h1>welcome to calculator</h1>You are the calculator! If you answer 20 questions correctly in 2 minutes," +
                        " you can proceed<br>You have currently answered 0 questions.<br>What is 61+99?<form method=\"POST\">" +
                        "<input name=\"answer\" type=\"text\"/>" +
                        "<input type=\"submit\">" +
                        "</form>")))
                .bind()
                .block();

        TimeUnit.SECONDS.sleep(10000);
    }
}
