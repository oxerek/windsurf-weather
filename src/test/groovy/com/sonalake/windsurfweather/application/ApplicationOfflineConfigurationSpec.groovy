package com.sonalake.windsurfweather.application

import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("offline")
class ApplicationOfflineConfigurationSpec extends ApplicationConfigurationSpec {

    def "should load offline context correctly"() {

        expect:
        1 == 1
    }
}
