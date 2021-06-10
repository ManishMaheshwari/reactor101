package com.manish.tcp;

import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

/**
 * For configuring options that are not available from Netty Channel's higher level constructs.
 * Common ChannelOption
 * Epoll ChannelOption
 * KQueue ChannelOption
 * Socket Options
 */
public class Tcp_03_Server_ChannelOptions {

    private static String HOST = "localhost";
    private static int PORT = 9999;
    private static boolean WIRETAP = true;

    public static final Logger LOGGER = LoggerFactory.getLogger(Tcp_03_Server_ChannelOptions.class);

    public static void main(String[] args) {

        DisposableServer server = TcpServer.create()
                .host(HOST)
                .port(PORT)
                .wiretap(WIRETAP)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) //If the remote machine does not answer during Connect operation (mostly used bu clients)
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
