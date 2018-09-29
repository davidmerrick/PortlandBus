package com.merricklabs.portlandbus.storage

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import com.merricklabs.portlandbus.PortlandBusConfig
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.OptionalInt

private val log = KotlinLogging.logger {}

class MyStopStorageImpl : MyStopStorage, KoinComponent {

    val config by inject<PortlandBusConfig>()

    private val dynamoDbConfig: PortlandBusConfig.DynamoDb
    private val table: Table

    init {
        this.dynamoDbConfig = config.dynamoDb

        val dynamoDbConfig = config.dynamoDb
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        AwsClientBuilder.EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region)
                )
                .build()

        val dynamoDB = DynamoDB(client)
        this.table = dynamoDB.getTable(dynamoDbConfig.tableName)
    }

    override fun saveStop(userId: String, stopId: Int) {
        log.info("Saving stop $stopId")

        val item = Item()
                .withPrimaryKey(dynamoDbConfig.userIdKey, userId)
                .withInt(dynamoDbConfig.stopIdKey, stopId)

        try {
            table.putItem(item)
        } catch (e: Exception) {
            log.error("Error saving stop $stopId: ${e.message}", e)
            throw RuntimeException(e)
        }
    }

    override fun queryStopId(userId: String): OptionalInt {
        val item = table.getItem(dynamoDbConfig.userIdKey, userId) ?: return OptionalInt.empty()
        return OptionalInt.of(item.getInt(dynamoDbConfig.stopIdKey))
    }
}
