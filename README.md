# windsurf-weather

Demo application written in reactive manner with Ports&Adapters architecture in java 11.
(in case of higher jvm's please add jvm flag: -XX:+AllowRedefinitionToAddDeleteMethods)

Main assumption: Mono and Flux from https://projectreactor.io/ are treated as a part of a language and can be used inside the domain.

The app allows to get the best location for windsurfing in terms of weather from spots list:

Jastarnia (Poland)
Bridgetown (Barbados)
Fortaleza (Brazil)
Pissouri (Cyprus)
Le Morne (Mauritius)

Weather data source is weatherbit api: https://www.weatherbit.io/api/weather-forecast-16-day

Additionally, the app uses the Open Api Generator to generate a model from https://www.weatherbit.io/static/swagger.json

All the necessary properties are included in the application in src/main/resources/application.properties

Building an application:

*./gradlew clean build*

Application can be run in two profiles: offline and online

*offline*: weatherbit.io will be requested on application start and then cyclically with cron exp (windsurf-weather.fetchSpotForecastsCron in application.properties)
fetched data will be stored in local in memory store:

*java -jar -Dspring.profiles.active=offline windsurf-weather-1.0.0-SNAPSHOT.jar*

or

*online*: weatherbit.io will be requested directly each time:

*java -jar -Dspring.profiles.active=online windsurf-weather-1.0.0-SNAPSHOT.jar*

It is also possible to run app in the docker:

*./gradlew docker*

*docker run -p 8080:8080 com.sonalake/windsurf-weather*

Request example: 

*curl --location --request GET 'http://localhost:8080/forecasts/best?day=2021-11-07'*

To add or remove spots locations please edit *spot-locations.csv* file and then execute:

*./gradlew clean runSpotLocationsEnumGenerator build*

# Requirements

1. Use the Weatherbit Forecast API(https://www.weatherbit.io/api/weather-forecast-16-day) as the weather data source.

2. Expose a REST API, where the argument is a day (yyyy-mm-dd date format) and return value is one of following locations:

	Jastarnia (Poland)
	Bridgetown (Barbados)
	Fortaleza (Brazil)
	Pissouri (Cyprus)
	Le Morne (Mauritius)

	depending on which place offers better windsurfing conditions on that day in the 16 forecast day range. Apart from returning the name of the location, the response should also include weather conditions (at least average temperature - Celcius, wind speed - m/s) for the location on that day.


3. The list of windsurfing locations (including geographical coordinates) should be embedded in the application in a way that allows for extensions at a later stage. Note: There is no API requirement for creating or editing the locations.

4. The best location selection criteria are:
	If the wind speed is not within <5; 18> (m/s) and the temperature is not in the range <5; 35> (°C), the location is not suitable for windsurfing. However, if they are in these ranges, then the best location is determined by the highest value calculated from the following formula:
	v * 3 + temp
	where v is the wind speed in m/s on a given day, and temp is an average forecasted temperature for a given day in Celsius, respectively - you can obtain these parameters from the “data” key in Weatherbit API’s response.
	If none of the locations meets the above criteria, the application does not return any.

5. The application is written in Java 8 or higher and uses Spring Boot. Frontend is not required.

6. It has a gradle or maven building mechanism and a README file describing at least the procedure for building and running the application.

7. Exceptional scenarios or requirements not specified above should be handled at your discretion.

8. Store your application code in a private BitBucket repo and share it with sonalake.recruitment@sonalake.com