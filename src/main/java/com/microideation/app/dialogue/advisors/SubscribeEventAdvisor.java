package com.microideation.app.dialogue.advisors;

import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.annotations.SubscribeEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sandheepgr on 18/6/16.
 */
@Aspect
@Component
public class SubscribeEventAdvisor {

    @Autowired
    ConnectionFactory connectionFactory;

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() {  }


    @Around(value = "anyPublicMethod() && @annotation(subscribeEvent)")
    public void subscribeEvent(ProceedingJoinPoint joinPoint,SubscribeEvent subscribeEvent) throws Throwable {

        // Check if the subscribeEvent is null
        if ( subscribeEvent == null ) {

            // error logging
            return;

        }



        System.out.println("Intercepted subscribeEvent - returnValue : ");

        joinPoint.proceed();
    }

}
