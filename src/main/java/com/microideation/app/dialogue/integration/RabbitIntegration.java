package com.microideation.app.dialogue.integration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private ConcurrentHashMap<String,Queue> rabbitChannels;


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

}
