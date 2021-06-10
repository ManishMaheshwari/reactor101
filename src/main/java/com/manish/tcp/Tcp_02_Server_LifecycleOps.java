package com.manish.tcp;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.util.concurrent.TimeUnit;

public class Tcp_02_Server_LifecycleOps {

    private static String HOST = "localhost";
    private static int PORT = 9999;
    private static boolean WIRETAP = true;

    public static final Logger LOGGER = LoggerFactory.getLogger(Tcp_02_Server_LifecycleOps.class);

    public static void main(String[] args) {

        DisposableServer server = TcpServer.create()
                .host(HOST)
                .port(PORT)
                .wiretap(WIRETAP) //Debug Logs
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
                .doOnBind(tcpServerConfig -> {
                    LOGGER.info("doOnBind: isSecure- {}", tcpServerConfig.isSecure());
                })
                .doOnBound(disposableServer -> {
                    LOGGER.info("doOnBound: Host {}, Port {}, Address {} ",
                            disposableServer.host(), disposableServer.port(), disposableServer.address());
                })
//                .doOnChannelInit(null)
                .doOnConnection(connection -> {
                    //Client socket connection.
                    connection.onReadIdle(15000, () -> {
                        //Consumer for read idle
                        LOGGER.info("Read Idle for 10s");
                        //connection.dispose();
                    });
                    connection.onWriteIdle(10000, () -> LOGGER.info("Write Idle for 10s"));//Finer control on SO_TIMEOUT type property.
                    connection.addHandler(new ReadTimeoutHandler(30, TimeUnit.SECONDS)); //Initiate Cx close after 30 sec of read timeout
                    connection.addHandler(new WriteTimeoutHandler(30, TimeUnit.SECONDS)); //Initiate Cx close after 30 sec of write timeout
                    LOGGER.info("doOnConnection - isPersistent {}, Socket Address {}",
                            connection.isPersistent(), connection.address().toString());
                })
                .doOnUnbound(disposableServer -> {
                    LOGGER.info("doOnUnbound");
                })
                .bindNow();

        server.onDispose()
                .block();
    }
}
