package com.sonalake.windsurfweather.domain.ports;

import reactor.core.publisher.Flux;

public interface Client<REQUEST, RESPONSE> {

    default Flux<RESPONSE> get(REQUEST request) {
        throw new UnsupportedOperationException();
    }
}
