/**
 * @package com.nopaper.work.gateway.config -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 7:15:49 pm
 * @git 
 */
package com.nopaper.work.gateway.config;

/**
 * 
 */

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
//import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.techmonks.apigateway.repository.**")
public class MongoReactiveConfiguration extends AbstractReactiveMongoConfiguration {


    @Value("${spring.data.mongodb.uri}")
    private String mongoDatabaseUri; 
//	= "mongodb+srv://anjireddykata:vsIV6BZfq0rzWbly@akata.doyruww.mongodb.net";
//	mongodb+srv://admin:<db_password>@metis-nopaper-gateway-c.ib1dlyw.mongodb.net/?retryWrites=true&w=majority&appName=metis-nopaper-gateway-cluster0";
    
    @Value("${spring.data.mongodb.database}")
    private String databaseName;// = "scg";

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoDatabaseUri);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }
}