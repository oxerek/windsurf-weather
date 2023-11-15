package com.sonalake.windsurfweather.domain.ports;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UseCase<INPUT, OUTPUT> {

    default Flux<OUTPUT> execute() {
        throw new UnsupportedOperationException();
    }

    default Mono<OUTPUT> execute(INPUT input) {
        throw new UnsupportedOperationException();
    }
}
