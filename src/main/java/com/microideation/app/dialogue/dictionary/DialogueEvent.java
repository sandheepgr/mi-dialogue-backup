package com.microideation.app.dialogue.dictionary;

import java.io.Serializable;

/**
 * Created by sandheepgr on 17/6/16.
 */
public class DialogueEvent implements Serializable{

    private String data;

    public DialogueEvent() {}

    public DialogueEvent(String data) {

        this.data=data;

    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "DialogueEvent{" +
                "data='" + data + '\'' +
                '}';
    }
}
