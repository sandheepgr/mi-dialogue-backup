package com.microideation.app.dialogue.service.impl;

import com.microideation.app.dialogue.annotations.DialogueEventListener;
import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.annotations.SubscribeEvent;
import com.microideation.app.dialogue.dictionary.DialogueEvent;
import com.microideation.app.dialogue.dictionary.EventStore;
import com.microideation.app.dialogue.service.SampleService;
import org.springframework.stereotype.Service;

/**
 * Created by sandheepgr on 17/6/16.
 */
@Service
@DialogueEventListener
public class SampleServiceImpl implements SampleService {


    @PublishEvent(channelName = "${data.queuename}", eventStore = EventStore.RABBITMQ)
    @Override
    public DialogueEvent test() {

        System.out.println("Inside test method1");
        return new DialogueEvent("this is test data1");

    }


    @SubscribeEvent(channelName = "${data.queuename}", eventStore = EventStore.RABBITMQ)
    public void test2(DialogueEvent dialogueEvent) {

        System.out.println("Received the event" + dialogueEvent);

    }

}
