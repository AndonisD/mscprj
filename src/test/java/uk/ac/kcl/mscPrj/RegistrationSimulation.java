package uk.ac.kcl.mscPrj;

import com.github.javafaker.Faker;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class RegistrationSimulation extends Simulation {
    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();

    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();

    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildLoginPostScenario();

    public RegistrationSimulation() {
//        setUp(POST_SCENARIO_BUILDER.injectOpen(incrementUsersPerSec(10).times(10)
//                        .eachLevelLasting(Duration.ofSeconds(3))
//                        .startingFrom(10))
//                .protocols(HTTP_PROTOCOL_BUILDER));
//        setUp(POST_SCENARIO_BUILDER.injectOpen(constantUsersPerSec(1).during(Duration.ofSeconds(10)))
//                .protocols(HTTP_PROTOCOL_BUILDER));
        setUp(POST_SCENARIO_BUILDER.injectOpen(postEndpointInjectionProfile())
                .protocols(HTTP_PROTOCOL_BUILDER)).assertions(
                global().responseTime().max().lte(10000),
                global().successfulRequests().percent().gt(90d));
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return http.baseUrl("http://localhost:8082")
                .acceptHeader("application/json")
                .maxConnectionsPerHost(10)
                .userAgentHeader("gatling/performance-testing");
    }

    private static ScenarioBuilder buildLoginPostScenario() {
        return CoreDsl.scenario("Login Users Test")
                .feed(FEED_DATA)
                .exec(http("login-user-request")
                        .post("/api/auth/login")
                        .header("Content-Type", "application/json")
                        .body(StringBody("{ " +
                                "\"username\": \"#{username}\", " +
                                "\"password\": \"#{password}\" " +
                                "}"))
                        .check(status().is(401)));
    }

    private static ScenarioBuilder buildRegisterPostScenario() {
        return CoreDsl.scenario("Load Test Registering Users")
                .feed(FEED_DATA)
                .exec(http("register-user-request")
                        .post("/api/auth/register")
                        .header("Content-Type", "application/json")
                        .body(StringBody("{ " +
                                "\"username\": \"#{username}\", " +
                                "\"email\": \"#{email}\", " +
                                "\"password\": \"#{password}\" " +
                                "}"))
                        .check(status().is(201)));
    }

    private static Iterator<Map<String, Object>> setupTestFeedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
                    Map<String, Object> stringObjectMap = new HashMap<>();
                    stringObjectMap.put("username", faker.name().username());
                    stringObjectMap.put("password", faker.internet().password());
                    return stringObjectMap;
                })
                .iterator();
        return iterator;
    }

    private OpenInjectionStep.RampRate.RampRateOpenInjectionStep postEndpointInjectionProfile() {
        int totalDesiredUserCount = 200;
        double userRampUpPerInterval = 5;
        double rampUpIntervalSeconds = 30;
        int totalRampUptimeSeconds = 120;
        int steadyStateDurationSeconds = 10;

        return rampUsersPerSec(userRampUpPerInterval / (rampUpIntervalSeconds / 60)).to(totalDesiredUserCount)
                .during(Duration.ofSeconds(totalRampUptimeSeconds + steadyStateDurationSeconds));
    }

}
