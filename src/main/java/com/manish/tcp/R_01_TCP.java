package com.manish.tcp;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class R_01_TCP {
    public static void main(String[] args) {
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        ConnectionProvider provider = ConnectionProvider.builder("fixed")
                .maxConnections(50)
                .pendingAcquireMaxCount(10)
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .maxIdleTime(Duration.ofSeconds(120))
                .maxLifeTime(Duration.ofSeconds(300))
                .build();

        Connection conn = TcpClient.create(provider)
                .host("www.google.com")
                .port(80)
                .handle((in, out) -> {
                    in.receive().then();
                    out.sendString(Mono.just("Hello"));
                    return null;
                }).connectNow();

    }
}
