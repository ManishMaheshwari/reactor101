package com.manish.tcp;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public class R_01_TCP {
    public static void main(String[] args) throws InterruptedException {

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

//        String HOST = "www.google.com";
//        int PORT = 80;

        String HOST = "localhost";
        int PORT = 9999;
        Connection conn = TcpClient.create(provider)
                .host(HOST)
                .port(PORT)
                .doOnConnect(tcpClientConfig -> {
                    System.out.format("doOnConnect - isSecure: %s%n", tcpClientConfig.isSecure());
                })
                .doOnChannelInit((connectionObserver, channel, remoteAddress) -> {
                    System.out.printf("doOnChannelInit - localAddress: %s, remoteAddress: %s%n", channel.localAddress(), channel.remoteAddress());
                })
                .doOnResolve(connection -> {
                    System.out.printf("doOnResolve - isPersistent: %s%n", connection.isPersistent());
                })
                .doAfterResolve((connection, socketAddress) -> {
                    System.out.printf("doAfterResolve - %s%n", socketAddress.toString());
                })
                .doOnConnected(connection -> {
                    System.out.printf("doOnConnected - isPersistent: %s, localAdd: %s, remoteAdd: %s%n", connection.isPersistent(),
                            connection.channel().localAddress(), connection.channel().remoteAddress());
                })
                .doOnDisconnected(connection -> {
                    System.out.printf("doOnDisconnected - isDisposed: %s%n", connection.isDisposed());
                })
                .handle((in, out) -> {
//                    in.receive().then();
                    System.out.println("HANDLER");
//                    in.receive()
                    in.receive().subscribe(System.out::println);
                    out.sendString(Mono.just("Hello"));
                    return Mono.empty();
                })
                .wiretap(true)
                .connectNow()
                ;
        TimeUnit.SECONDS.sleep(10);    }
}
