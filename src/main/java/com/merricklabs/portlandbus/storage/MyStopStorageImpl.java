package com.merricklabs.portlandbus.storage;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Singleton;
import com.merricklabs.portlandbus.PortlandBusConfig;
import java.util.OptionalInt;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class MyStopStorageImpl implements MyStopStorage {

    private final PortlandBusConfig.DynamoDb dynamoDbConfig;
    private final Table table;

    @Inject
    public MyStopStorageImpl(PortlandBusConfig config) {
        this.dynamoDbConfig = config.getDynamoDb();

        PortlandBusConfig.DynamoDb dynamoDbConfig = config.getDynamoDb();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(dynamoDbConfig.getEndpoint(), dynamoDbConfig.getRegion())
                )
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        this.table = dynamoDB.getTable(dynamoDbConfig.getTableName());
    }

    @Override
    public void saveStop(String userId, int stopId) {
        log.info("Saving stop {}", stopId);

        Item item = new Item()
                .withPrimaryKey(dynamoDbConfig.getUserIdKey(), userId)
                .withInt(dynamoDbConfig.getStopIdKey(), stopId);

        try {
            table.putItem(item);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public OptionalInt queryStopId(String userId) {
        Item item = table.getItem(dynamoDbConfig.getUserIdKey(), userId);

        if (item == null) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(item.getInt(dynamoDbConfig.getStopIdKey()));
    }
}
