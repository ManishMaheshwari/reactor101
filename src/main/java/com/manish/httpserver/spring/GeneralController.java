package com.manish.httpserver.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class GeneralController {

    private static Logger LOGGER = LoggerFactory.getLogger(GeneralController.class);

    @GetMapping("/hello")
    public String handle() {
        return "Hello WebFlux";
    }

    @GetMapping("/mono/{data}")
    public Mono<String> mono(@PathVariable String data) {
        LOGGER.info("Handling mono: {}", data);
        return Mono.just(data.toUpperCase());
    }

    @GetMapping("/mono/{data}/{delay}")
    public Mono<String> monoDelayed(@PathVariable String data, @PathVariable int delay) {
        LOGGER.info("Handling monoDelayed - Data - {}, Delay - {}ms", data, delay);
        return Mono.just(String.format("%s delayed by %d ms.", data.toUpperCase(), delay))
                .delayElement(Duration.ofMillis(delay));
    }

    @GetMapping("/flux/{count}")
    public Flux<Integer> flux(@PathVariable int count) {
        LOGGER.info("Handling flux - Count - {}", count);
        return Flux.range(100, count);
    }

    @GetMapping("/flux/{count}/{delay}")
    public Flux<Integer> fluxDelayed(@PathVariable int count, @PathVariable int delay) {
        LOGGER.info("Handling fluxDelayed - Count - {}, Delay - {}ms", count, delay);
        return Flux.range(100, count)
//                .delaySequence(Duration.ofMillis(1000))
                .delayElements(Duration.ofMillis(delay));
    }

}
