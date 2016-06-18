package com.microideation.app.dialogue.advisors;

import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.dictionary.EventStore;
import com.microideation.app.dialogue.integration.RabbitIntegration;
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

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() {  }


    @AfterReturning(value = "anyPublicMethod() && @annotation(publishEvent)",returning = "returnValue")
    public void publishEvent(JoinPoint joinPoint,Object returnValue,PublishEvent publishEvent) throws Throwable {

        // Check if the publishEvent is null
        if ( publishEvent == null ) {

            // error logging
            return;

        }

        // call the processPublishEvent method for processing
        processPublishEvent(publishEvent,returnValue);

        System.out.println("Intercepted publishEvent - returnValue : " + returnValue);

    }


    /**
     * Method to publish the event to the specified channel and type
     *
     * @param publishEvent  : The received PublishEvent annotation object
     * @param payload       : The payload from the object
     */
    private void processPublishEvent(PublishEvent publishEvent,Object payload) {

        // Get the channelName
        String channelName = publishEvent.channelName();

        // Get the type
        EventStore eventStore = publishEvent.eventStore();

        // Get the isPersistent
        boolean isPersistent = publishEvent.isPersistent();


        // Switch the event type
        switch (eventStore) {

            // Processing for the rabbitmq
            case RABBITMQ:

                // Call the publishToQueue method of the rabbitIntegration
                rabbitIntegration.publishToQueue(channelName,isPersistent,payload);

                // Break
                break;


            // Processing for the redis type
            case REDIS:

                // break
                break;

        }

    }

}
