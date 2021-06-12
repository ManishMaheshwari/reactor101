package com.manish.httpserver.reactor;

import com.manish.util.FluxFactory;
import com.manish.util.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestHttpServer {

    public static void main(String[] args) throws InterruptedException {
        HttpServer.create()
                .host("0.0.0.0")
                .port(9898)
                .route(routes ->
                        routes
                                .get("/mono-hello",
                                        (request, response) -> response.sendString(Mono.just("Hello World!")))

                                .post("/echo",
                                        (request, response) -> response.send(request.receive().retain()))

                                .get("/flux/{count}/{delay}/{error}",
                                        (request, response) -> {
                                            Map<String, String> params = request.params();
                                            int count = params.get("count")!=null? Integer.valueOf(params.get("count")).intValue(): 1;
                                            int delay = params.get("delay")!=null? Integer.valueOf(params.get("delay")).intValue(): 0;
                                            boolean error = params.get("error")!=null? Boolean.valueOf(params.get("error")).booleanValue(): false;
                                            Flux<Person> flux = FluxFactory.getFlux(count, delay, error);
                                            return response.sendObject(flux);
//                                            return response.sendString(Mono.just(request.param("param")));
                                        })

                                .ws("/ws",
                                        (wsInbound, wsOutbound) -> wsOutbound.send(wsInbound.receive().retain())))
                .bindNow();


//                .handle((req, res) -> {
//                    Map<String, String> params = req.params();
//                    System.out.println(req.uri());
//
//                    System.out.println("--------->  " + req.param("count"));
//                    int count = Integer.valueOf(params.get("count")).intValue();
//                    int delay = Integer.valueOf(params.get("delay")).intValue();
//                    boolean error = Boolean.valueOf(params.get("error")).booleanValue();
//                    Flux<Person> flux = FluxFactory.getFlux(count, delay, error);
//
//                    return res.sendObject(flux);
//
////                    return res.sendObject("mm");
//                })
//                .bind()
//                .block();

        TimeUnit.SECONDS.sleep(10000);
    }
}


