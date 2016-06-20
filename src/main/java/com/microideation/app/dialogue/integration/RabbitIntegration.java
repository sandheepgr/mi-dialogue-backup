package com.microideation.app.dialogue.integration;

import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.event.DialogueEvent;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sandheepgr on 18/6/16.
 */
@Component
public class RabbitIntegration implements DialogueIntegration {



    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private IntegrationUtils integrationUtils;


    @Resource
    private ConcurrentHashMap<String,Queue> rabbitChannels;

    @Resource
    private ConcurrentHashMap<String,SimpleMessageListenerContainer> rabbitContainers;



    /**
     * Method to build the queue using the autowired rabbit configuration
     *
     * @param queueName : The name of the queue
     * @param persist   : Flag showing whether the queue need to be persistent or not
     *
     * @return          : Return the queue object created
     */
    public Queue buildQueue(String queueName,boolean persist) {

        // Check if the queue is already existing
        if ( rabbitChannels.containsKey(queueName) ) {

            // return the queue from the list
            return rabbitChannels.get(queueName);

        }

        // Declare the queue object
        Queue queue = new Queue(queueName,persist);

        //add a topic exchange for the queue
        TopicExchange exchange = new TopicExchange(queueName);

        //declare the queue
        amqpAdmin.declareQueue(queue);

        //declare the exchange
        amqpAdmin.declareExchange(exchange);

        //add binding for queue and exchange
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(queueName));

        // Add the queue to the channels list
        rabbitChannels.put(queueName,queue);

        //  return the queue
        return queue;

    }

    /**
     * Method to create the SimpleMessageListener object for the queue for subscribing
     *
     * @param listener      : The listener class object
     * @param methodName    : The name of the method for the listener
     * @param queueName     : The name of the queue for which this listener is enabled.
     *
     * @return              : Return the SimpleMessageListenerContainer object
     */
    public SimpleMessageListenerContainer createListenerContainer(Object listener,String methodName,String queueName) {

        // If the queue already contains the listener, then return the instance
        if ( rabbitContainers.containsKey(queueName) ) {

            // TO-DO : Throw the execption that this container cannot have more than
            // one subscriber
            return rabbitContainers.get(queueName);

        }


        // Build the queue
        // If the queue is already existing, this will fail.
        buildQueue(queueName,false);

        //add a message listener for the queue , receiver object is also created for each smsChannel
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter( listener ,methodName);

        //add a messageListenerContainer for the receiver , and set the parameters
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueueNames(queueName);
        simpleMessageListenerContainer.setConcurrentConsumers(5);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
        simpleMessageListenerContainer.afterPropertiesSet();

        //start the receiver container
        simpleMessageListenerContainer.start();

        // Add to the queue
        rabbitContainers.put(queueName,simpleMessageListenerContainer);

        // Return the listener
        return simpleMessageListenerContainer;

    }



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
    public Object publishToChannel(PublishEvent publishEvent,DialogueEvent dialogueEvent) {

        // Get the property value for the channelName
        String channelName = integrationUtils.getEnvironmentProperty(publishEvent.channelName());

        // Get the queue
        Queue queue = buildQueue(channelName,publishEvent.isPersistent());

        // If the queue is null, return false
        if ( queue == null ) return false;

        // Send to the queue using the rabbitTemplate
        rabbitTemplate.convertAndSend(channelName,dialogueEvent);

        // return the object passed;
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
    public void registerSubscriber(Object listenerClass,String methodName, String channelName) {


        // Get the property value for the channelName
        channelName = integrationUtils.getEnvironmentProperty(channelName);

        // Call the method to create the listener
        createListenerContainer(listenerClass,methodName,channelName);

    }


    /**
     * Method to be called when the spring context is finishing
     * This will call the stop on the containers
     */
    @PreDestroy
    @Override
    public void stopListeners() {

        // Iterate the through the containers and stop them
        for ( SimpleMessageListenerContainer container : rabbitContainers.values() ) {

            container.stop();

        }

    }

}
