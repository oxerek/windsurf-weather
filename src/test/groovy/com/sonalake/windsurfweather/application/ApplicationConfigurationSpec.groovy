package com.sonalake.windsurfweather.application

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@TestPropertySource("classpath:test.properties")
@SpringBootTest(classes = ApplicationInitializer.class, webEnvironment = RANDOM_PORT)
class ApplicationConfigurationSpec extends Specification {

}
