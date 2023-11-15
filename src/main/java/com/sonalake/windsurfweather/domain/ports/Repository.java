package com.sonalake.windsurfweather.domain.ports;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Repository<ENTITY> {

    default Flux<ENTITY> findAll() {
        throw new UnsupportedOperationException();
    }

    default Mono<ENTITY> save(ENTITY entity) {
        throw new UnsupportedOperationException();
    }
}
