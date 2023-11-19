package idk.bluecross.messenger.config

import com.mongodb.MongoClientSettings
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import java.util.concurrent.TimeUnit


@EnableReactiveMongoRepositories
class MongoConfiguration : AbstractMongoClientConfiguration() {

    @Value("spring.data.mongodb.transaction.timeout")
    private var TRANSACTION_TIMEOUT: Long = 5000

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