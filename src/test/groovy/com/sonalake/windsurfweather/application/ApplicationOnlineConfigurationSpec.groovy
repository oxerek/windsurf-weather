package com.sonalake.windsurfweather.application

import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("online")
class ApplicationOnlineConfigurationSpec extends ApplicationConfigurationSpec {

    def "should load online context correctly"() {

        expect:
        1 == 1
    }
}
