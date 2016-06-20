package com.microideation.app.dialogue.integration;

import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.event.DialogueEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sandheepgr on 20/6/16.
 */
@Component
public class RedisIntegration implements DialogueIntegration{

    @Autowired
    private RedisTemplate<String,DialogueEvent> dialogueRedisTemplate;

    @Autowired
    RedisConnectionFactory connectionFactory;

    @Autowired
    private IntegrationUtils integrationUtils;

    @Resource
    private  ConcurrentHashMap<String,RedisMessageListenerContainer> redisContainers;



    /**
     * Method to publish an item to the queue
     *
     * @param publishEvent : The instance of publishEvent annotation
     * @param dialogueEvent: The object to be sent
     *
     * @return          : Return the object if the publish was successful
     *                    Return null otherwise
     */
    @Override
    public Object publishToChannel(PublishEvent publishEvent, DialogueEvent dialogueEvent) {

        // Get the property value for the channelName
        String channelName = integrationUtils.getEnvironmentProperty(publishEvent.channelName());

        // Send to the channel
        dialogueRedisTemplate.convertAndSend(channelName,dialogueEvent);

        // return the object
        return dialogueEvent;

    }

    /**
     * Overridden method to register the subscriber
     * @param listenerClass : The listener class object
     * @param methodName    : The name of the method for the listener
     * @param channelName   : The name of the queue for which this listener is enabled.
     *
     */
    @Override
    public void registerSubscriber(Object listenerClass, String methodName, String channelName) {

        // Get the property value for the channelName
        channelName = integrationUtils.getEnvironmentProperty(channelName);

        // Create the key
        String key = channelName+"#"+methodName;

        // If the queue already contains the listener, then return the instance
        if ( redisContainers.containsKey(key) ) {

            // TO-DO : Throw the execption that this container cannot have more than
            // one subscriber
            return;

        }

        // Create the MessageListenerAdapter
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(listenerClass,methodName);
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(DialogueEvent.class));
        messageListenerAdapter.afterPropertiesSet();

        // Create the listener container
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic(channelName));
        container.afterPropertiesSet();

        // Start the receiver container
        container.start();

        // Add to the containers list
        redisContainers.put(key,container);


    }

    /**
     * Method to be called when the spring context is finishing
     * This will call the stop on the containers
     */
    @PreDestroy
    @Override
    public void stopListeners() {

        // Iterate the through the containers and stop them
        for ( RedisMessageListenerContainer container : redisContainers.values() ) {

            container.stop();

        }


    }


}
