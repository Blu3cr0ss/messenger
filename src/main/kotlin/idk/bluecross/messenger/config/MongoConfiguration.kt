package idk.bluecross.messenger.config

import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.mongo.ReactiveMongoClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import java.util.concurrent.TimeUnit


@EnableReactiveMongoRepositories
class MongoConfiguration : AbstractReactiveMongoConfiguration() {

    @Value("spring.data.mongodb.transaction.timeout")
    private var TRANSACTION_TIMEOUT: Long = 5000

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create()
    }

    override fun getDatabaseName(): String {
        return "messenger"
    }

    @Bean
    fun mongoSettings(): MongoClientSettings {
        return MongoClientSettings.builder()
            .applyToClusterSettings { it.serverSelectionTimeout(TRANSACTION_TIMEOUT, TimeUnit.MILLISECONDS) }
            .build()
    }
}