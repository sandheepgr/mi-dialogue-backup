package com.microideation.app.dialogue.integration;

import com.microideation.app.dialogue.annotations.PublishEvent;
import com.microideation.app.dialogue.dictionary.DialogueEvent;

import javax.annotation.PreDestroy;

/**
 * Created by sandheepgr on 20/6/16.
 */
public interface DialogueIntegration {

    public Object publishToChannel(PublishEvent publishEvent,DialogueEvent dialogueEvent);
    public void registerSubscriber(Object listenerClass,String methodName, String channelName);
    @PreDestroy
    public void stopListeners();

}
