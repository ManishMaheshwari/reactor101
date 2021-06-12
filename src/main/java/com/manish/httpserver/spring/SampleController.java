package com.manish.httpserver.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SampleController {

    private static Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @GetMapping("/hello")
    public String handle() {
        return "Hello WebFlux";
    }

    private Scheduler scheduler = Schedulers.boundedElastic();

    @GetMapping("/mono/{data}")
    public Mono<String> mono(@PathVariable String data){
        LOGGER.info("Handling mono: {}", data);
        return Mono.just(data.toUpperCase());
    }

    @GetMapping("/mono/{data}/{delay}")
    public Mono<String> monoDelayed(@PathVariable String data, @PathVariable int delay){
        LOGGER.info("Handling monoDelayed - Data - {}, Delay - {}ms", data, delay);
        return Mono.just(String.format("%s delayed by %d ms.", data.toUpperCase(), delay))
                .delayElement(Duration.ofMillis(delay));
    }

    @GetMapping("/flux/{count}")
    public Flux<Integer> flux(@PathVariable int count){
        LOGGER.info("Handling flux - Count - {}", count);
        return Flux.range(100, count);
    }

    @GetMapping("/flux/{count}/{delay}")
    public Flux<Integer> fluxDelayed(@PathVariable int count, @PathVariable int delay){
        LOGGER.info("Handling fluxDelayed - Count - {}, Delay - {}ms", count, delay);
        return Flux.range(100, count)
//                .delaySequence(Duration.ofMillis(1000))
                .delayElements(Duration.ofMillis(delay));
    }

//    @GetMapping("/echos/x")
//    public Mono<Result> count() {
//        LOGGER.info("Handling /parallel");
//        return Flux.range(1, 10) // <1>
//                .log() //
//                .flatMap( // <2>
//                        value -> Mono.fromCallable(() -> block(value)) // <3>
//                                .subscribeOn(scheduler), // <4>
//                        4) // <5>
//                .collect(Result::new, Result::add) // <6>
//                .doOnSuccess(Result::stop); // <7>

        // <1> make 10 calls
        // <2> drop down to a new publisher to process in parallel
        // <3> blocking code here inside a Callable to defer execution
        // <4> subscribe to the slow publisher on a background thread
        // <5> concurrency hint in flatMap
        // <6> collect results and aggregate into a single object
        // <7> at the end stop the clock

    }

//    @RequestMapping("/serial")
//    public Mono<Result> serial() {
//        Scheduler scheduler = Schedulers.parallel();
//        LOGGER.info("Handling /serial");
//        return Flux.range(1, 10) // <1>
//                .log() //
//                .map( // <2>
//                        this::block) // <3>
//                .collect(Result::new, Result::add) // <4>
//                .doOnSuccess(Result::stop) // <5>
//                .subscribeOn(scheduler); // <6>
//        // <1> make 10 calls
//        // <2> stay in the same publisher chain
//        // <3> blocking call not deferred (no point in this case)
//        // <4> collect results and aggregate into a single object
//        // <5> at the end stop the clock
//        // <6> subscribe on a background thread
//    }

//    @RequestMapping("/netty")
//    public Mono<Result> netty() {
//        log.info("Handling /netty");
//        return Flux.range(1, 10) // <1>
//                .log() //
//                .flatMap(this::fetch) // <2>
//                .collect(Result::new, Result::add) //
//                .doOnSuccess(Result::stop); // <3>
//
//        // <1> make 10 calls
//        // <2> drop down to a new publisher to process in parallel
//        // <3> at the end stop the clock
//
//    }




