package com.microideation.app.dialogue.integration;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sandheepgr on 18/6/16.
 */
@Component
public class RabbitIntegration {



    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private ConnectionFactory connectionFactory;


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
     * Method to publish an item to the queue
     *
     * @param queueName : Name of the queue to which we need to publish
     * @param persist   : Flag indicating whether the queue need to be persist
     * @param object    : The object to be published
     *
     * @return          : Return true if the publish was successful
     *                    Return false otherwise
     */
    public boolean publishToQueue(String queueName, boolean persist , Object object) {

        // Get the queue
        Queue queue = buildQueue(queueName,persist);

        // If the queue is null, return false
        if ( queue == null ) return false;

        // Send to the queue using the rabbitTemplate
        rabbitTemplate.convertAndSend(queueName,object);

        // return true;
        return true;

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
     * Method to be called when the spring context is finishing
     * This will call the stop on the containers
     */
    @PreDestroy
    public void stopContainers() {

        // Iterate the through the containers and stop them
        for ( SimpleMessageListenerContainer container : rabbitContainers.values() ) {

            container.stop();

        }

    }

/*

    protected boolean isQueueExists(String queueName) {

        // Create the connection
        Connection connection = connectionFactory.createConnection();

        // Create the Channel
        Channel channel = connection.createChannel(false)

        try {

            // Check the result
            DeclareOk result = channel.queueDeclarePassive(queueName);

            // Return the boolean expression
            return result != null;

        } catch (IOException e) {

            // On exception , check if there is RESOURCE_LOCKED which means
            // queue is existing
            return e.getCause().getMessage().contains("RESOURCE_LOCKED");

        } finally {

            // finally close the connection
            connection.close();

        }

    }*/

}
