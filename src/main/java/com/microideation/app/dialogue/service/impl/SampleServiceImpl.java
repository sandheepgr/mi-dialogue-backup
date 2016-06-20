package com.microideation.app.dialogue.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microideation.app.dialogue.annotations.DialogueEventListener;
import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.annotations.SubscribeEvent;
import com.microideation.app.dialogue.dictionary.DialogueEvent;
import com.microideation.app.dialogue.dictionary.EventStore;
import com.microideation.app.dialogue.dictionary.TestType;
import com.microideation.app.dialogue.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by sandheepgr on 17/6/16.
 */
@Service
@DialogueEventListener
public class SampleServiceImpl implements SampleService {


    @PublishEvent(channelName = "${data.queuename}", eventStore = EventStore.RABBITMQ)
    @Override
    public TestType publishToRabbit() {

        return new TestType("This is test data for rabbit");

    }


    @SubscribeEvent(channelName = "${data.queuename}", eventStore = EventStore.RABBITMQ)
    public void subscriteToRabbit(DialogueEvent dialogueEvent) {

        TestType data = dialogueEvent.getPayload(TestType.class);

        System.out.println("Received the event from rabbit" + data);

    }


    @PublishEvent(channelName = "com.redis.channel", eventStore = EventStore.REDIS)
    @Override
    public TestType publishToRedis() {

        return new TestType("This is test data for redis");

    }


    @SubscribeEvent(channelName = "com.redis.channel", eventStore = EventStore.REDIS)
    public void subscriteToRedis(DialogueEvent dialogueEvent) {

        TestType data = dialogueEvent.getPayload(TestType.class);

        System.out.println("Received the event from redis" + data);

    }

}
