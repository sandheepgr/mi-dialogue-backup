package com.microideation.app.dialogue.advisors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.event.DialogueEvent;
import com.microideation.app.dialogue.event.EventStore;
import com.microideation.app.dialogue.integration.RabbitIntegration;
import com.microideation.app.dialogue.integration.RedisIntegration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sandheepgr on 17/6/16.
 */
@Aspect
@Component
public class PublishEventAdvisor {


    @Autowired
    private RabbitIntegration rabbitIntegration;

    @Autowired
    private RedisIntegration redisIntegration;

    @Autowired
    private ObjectMapper objectMapper;


    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() {  }


    @AfterReturning(value = "anyPublicMethod() && @annotation(publishEvent)",returning = "returnValue")
    public void publishEvent(JoinPoint joinPoint,Object returnValue,PublishEvent publishEvent) throws Throwable {

        // Check if the publishEvent is null
        if ( publishEvent == null ) {

            // error logging
            return;

        }

        // Set the data
        String json = objectMapper.writeValueAsString(returnValue);

        // Create the DialogueEvent
        DialogueEvent dialogueEvent = new DialogueEvent(json);

        // call the processPublishEvent method for processing
        processPublishEvent(publishEvent,dialogueEvent);

    }


    /**
     * Method to publish the event to the specified channel and type
     *
     * @param publishEvent  : The received PublishEvent annotation object
     * @param dialogueEvent : The payload from the object
     */
    private void processPublishEvent(PublishEvent publishEvent,DialogueEvent dialogueEvent) {

        // Get the type
        EventStore eventStore = publishEvent.eventStore();

        // Switch the event type
        switch (eventStore) {

            // Processing for the rabbitmq
            case RABBITMQ:

                // Call the publishToQueue method of the rabbitIntegration
                rabbitIntegration.publishToChannel(publishEvent, dialogueEvent);

                // Break
                break;


            // Processing for the redis type
            case REDIS:

                // Call the publishToQueue method of the rabbitIntegration
                redisIntegration.publishToChannel(publishEvent, dialogueEvent);

                // break
                break;

        }

    }

}
