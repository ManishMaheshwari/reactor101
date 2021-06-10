package com.manish.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

/**
 * Uppercase server
 */
public class Tcp_01_Server_Basic {

    private static String HOST = "localhost";
    private static int PORT = 9999;
    private static boolean WIRETAP = true;

    public static final Logger LOGGER = LoggerFactory.getLogger(Tcp_01_Server_Basic.class);

    public static void main(String[] args) {

        DisposableServer server = TcpServer.create()
                .host(HOST)
                .port(PORT)
                .handle((inbound, outbound) -> {
                            Flux<Void> voidFlux = inbound.receive()
                                    .asString()
                                    .flatMap(data -> {

                                        //consume data the way you like here.
                                        LOGGER.info(data);

                                        //Response to client - can be Flux, or Mono.
                                        //sendString, sendFile, sendByteArray, sendObject
                                        return outbound.sendString(Mono.just(data.toUpperCase()));
                                    });
                            return voidFlux;
                        }
                )
                .bindNow();

        server.onDispose()
                .block();
    }
}
