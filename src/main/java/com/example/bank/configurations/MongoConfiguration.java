package com.example.bank.configurations;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

@EnableMongoRepositories(basePackages = "com.example.bank.dao", mongoTemplateRef = "bankTablesMongoTemplate")
@Configuration
public class MongoConfiguration {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public MongoTemplate bankTablesMongoTemplate() {

        String uri = "mongodb://localhost:27017";
        String dbName = "EXAMPLE_BANK";
        ConnectionString connectionString = new ConnectionString(uri);
        final MongoClientSettings clientSettings = MongoClientSettings.builder()
                .retryWrites(true)
                .applyToConnectionPoolSettings((ConnectionPoolSettings.Builder builder) -> {
                    builder.maxSize(50) //connections count
                            .minSize(1)
                            .maxConnectionLifeTime(0, TimeUnit.MILLISECONDS)
                            .maxConnectionIdleTime(0, TimeUnit.MILLISECONDS)
                            .maxWaitTime(50000, TimeUnit.MILLISECONDS);
                })
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(50000, TimeUnit.MILLISECONDS);
                })
                .applyConnectionString(connectionString)
                .applicationName("bank")
                .build();


        return new MongoTemplate(MongoClients.create(clientSettings), dbName);
    }

}
