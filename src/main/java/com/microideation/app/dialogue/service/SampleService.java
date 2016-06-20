package com.microideation.app.dialogue.service;

import com.microideation.app.dialogue.dictionary.DialogueEvent;
import com.microideation.app.dialogue.dictionary.TestType;

/**
 * Created by sandheepgr on 17/6/16.
 */
public interface SampleService {

    public TestType publishToRabbit();
    public void subscriteToRabbit(DialogueEvent dialogueEvent);
    public TestType publishToRedis();
    public void subscriteToRedis(DialogueEvent dialogueEvent);

}
