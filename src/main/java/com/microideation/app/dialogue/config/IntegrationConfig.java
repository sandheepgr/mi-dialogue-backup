package com.microideation.app.dialogue.config;

import com.microideation.app.dialogue.dictionary.DialogueEvent;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sandheepgr on 18/6/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.microideation.app.dialogue.integration"})
public class IntegrationConfig {


    /** General configuration **/
    @Bean
    public ConcurrentHashMap<String,Queue> rabbitChannels() {

        return new ConcurrentHashMap<>(0);

    }

    @Bean
    public ConcurrentHashMap<String,SimpleMessageListenerContainer> rabbitContainers() {

        return new ConcurrentHashMap<>(0);

    }

    @Bean
    public ConcurrentHashMap<String,RedisMessageListenerContainer> redisContainers() {

        return new ConcurrentHashMap<>(0);

    }


    /** Redis configuration **/
    @Bean
    public RedisTemplate<String,DialogueEvent> dialogueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String,DialogueEvent> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(DialogueEvent.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate ;

    }
}
