package com.merricklabs.portlandbus;

import javax.inject.Singleton;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Singleton
@Value
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PortlandBusConfig {

    @Value
    public class Trimet {
        private final String appId = System.getenv("TRIMET_APP_ID");
        private final String arrivalsEndpoint = "https://developer.trimet.org/ws/V1/arrivals";
    }

    @Value
    public class Alexa {
        private final String skillId = System.getenv("SKILL_ID");
        private final String invocationName = System.getenv("INVOCATION_NAME");
        private final String stopIdSlot = "stopId";
    }

    @Value
    public class DynamoDb {
        private final String endpoint =
                System.getenv("DYNAMODB_ENDPOINT") != null ?
                        System.getenv("DYNAMODB_ENDPOINT") : "https://dynamodb.us-west-2.amazonaws.com";
        private final String region =
                System.getenv("DYNAMODB_REGION") != null ?
                        System.getenv("DYNAMODB_REGION") : "us-west-2";
        private final String tableName = System.getenv("DYNAMODB_TABLE");
        private final String userIdKey = "UserId";
        private final String stopIdKey = "StopId";
    }

    private Trimet trimet = new Trimet();
    private Alexa alexa = new Alexa();
    private DynamoDb dynamoDb = new DynamoDb();
}
