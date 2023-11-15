package com.sonalake.windsurfweather.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@Import(ApplicationConfiguration.class)
public class ApplicationInitializer {

	@SuppressWarnings("all")
	public static void main(String[] args) {
		BlockHound.install(builder -> builder
				.allowBlockingCallsInside("io.netty.handler.ssl.SslContext", "newClientContextInternal")
				.allowBlockingCallsInside("io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider", "loadNativeLibrary")
				.allowBlockingCallsInside("org.springframework.web.reactive.function.client.DefaultClientResponse", "bodyToMono")
				.blockingMethodCallback(blockingMethod -> new Error(blockingMethod.toString()).printStackTrace()));

		SpringApplication.run(ApplicationInitializer.class, args);
	}
}
