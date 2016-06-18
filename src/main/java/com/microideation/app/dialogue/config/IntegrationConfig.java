package com.microideation.app.dialogue.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sandheepgr on 18/6/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.microideation.app.dialogue.integration"})
public class IntegrationConfig {

    @Bean
    public ConcurrentHashMap<String,Queue> rabbitChannels() {

        return new ConcurrentHashMap<>(0);

    }



    @Bean
    public ConcurrentHashMap<String,SimpleMessageListenerContainer> rabbitContainers() {

        return new ConcurrentHashMap<>(0);

    }
}
