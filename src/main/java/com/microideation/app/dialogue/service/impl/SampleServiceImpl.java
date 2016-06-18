package com.microideation.app.dialogue.service.impl;

import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.dictionary.EventPayload;
import com.microideation.app.dialogue.dictionary.PublishEventType;
import com.microideation.app.dialogue.service.SampleService;
import org.springframework.stereotype.Service;

/**
 * Created by sandheepgr on 17/6/16.
 */
@Service
public class SampleServiceImpl implements SampleService {


    @PublishEvent(channelName = "com.inspirenetz.test2",publishType = PublishEventType.RABBITMQ)
    @Override
    public EventPayload test() {

        System.out.println("Inside test method");
        return new EventPayload("this is test data");

    }


}
