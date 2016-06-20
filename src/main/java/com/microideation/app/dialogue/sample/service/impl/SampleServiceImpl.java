package com.microideation.app.dialogue.sample.service.impl;

import com.microideation.app.dialogue.annotations.DialogueEventListener;
import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.annotations.SubscribeEvent;
import com.microideation.app.dialogue.event.DialogueEvent;
import com.microideation.app.dialogue.event.EventStore;
import com.microideation.app.dialogue.event.TestType;
import com.microideation.app.dialogue.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by sandheepgr on 17/6/16.
 */
@Profile("dev")
@Service
@DialogueEventListener
public class SampleServiceImpl implements SampleService {

    @Value("${dialogue.rabbit.channel}")
    private String channelName;

    @PublishEvent(channelName = "${dialogue.rabbit.channel}", eventStore = EventStore.RABBITMQ)
    @Override
    public TestType publishToRabbit() {

        return new TestType("This is test data for rabbit");

    }


    @SubscribeEvent(channelName = "${dialogue.rabbit.channel}", eventStore = EventStore.RABBITMQ)
    public void subscriteToRabbit(DialogueEvent dialogueEvent) {

        TestType data = dialogueEvent.getPayload(TestType.class);

        System.out.println("Received the event from rabbit" + data);

    }


    @PublishEvent(channelName = "${dialogue.redis.channel}", eventStore = EventStore.REDIS)
    @Override
    public TestType publishToRedis() {

        return new TestType("This is test data for redis");

    }


    @SubscribeEvent(channelName = "${dialogue.redis.channel}", eventStore = EventStore.REDIS)
    public void subscriteToRedis(DialogueEvent dialogueEvent) {

        TestType data = dialogueEvent.getPayload(TestType.class);

        System.out.println("Received the event from redis" + data);

    }

}
