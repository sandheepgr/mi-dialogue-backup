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

    @Autowired
    private ObjectMapper objectMapper;

    @PublishEvent(channelName = "${data.queuename}", eventStore = EventStore.RABBITMQ)
    @Override
    public TestType test() {

        System.out.println("Inside test method1");
        return new TestType("this is test data1");

    }


    @SubscribeEvent(channelName = "${data.queuename}", eventStore = EventStore.RABBITMQ)
    public void test2(DialogueEvent dialogueEvent) {

        TestType data = dialogueEvent.getPayload(TestType.class);

        System.out.println("Received the event" + data);

    }

}
