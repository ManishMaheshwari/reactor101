package com.manish;

import com.manish.util.Helper;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;

public class Reactor_019_Five_Favourites {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reactor_019_Five_Favourites.class);

    /**
     * Composing chain of functionalities
     * 1. Show details of top 5 favourite movies for a user
     * 2. If there are none, suggest a few
     *
     * See the flow, and alternate flows
     */
    public static void main(String[] args) throws InterruptedException {

        Helper.divider("By default, all events happen in the subscriber's thread (main)");

        int userId = 1001;
        new UserService()
                .getFavourite(userId) //Gets favoutite movies of user from UserService
                .timeout(Duration.ofMillis(2000)) // Time out if UserService is slow
                .onErrorResume(err -> new CacheService().getFavourite(userId)) //Try cache if time out happens
                .switchIfEmpty(new SuggestionService().getSuggestions(userId)) //If nothing found, use SuggestionService
                .flatMap(movie -> new MovieDetailService().getMovieDetail(movie) //Get details of movies from MovieDetailServices
                    .publishOn(Schedulers.boundedElastic()))
                .take(5)
                .subscribe(movDetail ->
                        System.out.printf("Movie: %s, Released in: %s%n", movDetail.name, movDetail.year),
                        System.out::println);

        Helper.hold(10);

    }
}


class UserService{
    Flux<Movie> getFavourite(int userId){
        if(userId!=1001) return Flux.empty();
        return Flux.just(new Movie("Mirage"),
                new Movie("First Man"),
                new Movie("12 Strong"),
                new Movie("Alpha"),
                new Movie("Terminal"))
                .delayElements(Duration.ofMillis(600)); //Try with higher time, to get from cache.
    }
}

class CacheService{
    Flux<Movie> getFavourite(int userId){
        if(userId!=1001) return Flux.empty();
        return Flux.just(new Movie("Cached Mirage"),
                new Movie("Cached First Man"),
                new Movie("Cached 12 Strong"),
                new Movie("Cached Alpha"),
                new Movie("Cached Terminal"));
    }
}

class MovieDetailService{
    Mono<MovieDetail> getMovieDetail(Movie movie){
        return Mono.just(new MovieDetail(movie.name, 2018))
                .delayElement(Duration.ofMillis(100));
    }
}

class SuggestionService{
    //Try with userId 2000 to get into Suggestions.
    Flux<Movie> getSuggestions(int userId){
        return Flux.just(new Movie("Suggesting Mirage"),
                new Movie("Suggesting First Man"),
                new Movie("Suggesting 12 Strong"),
                new Movie("Suggesting Alpha"),
                new Movie("Suggesting Terminal"));
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class Movie {
    String name;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class MovieDetail {
    String name;
    int year;
}

