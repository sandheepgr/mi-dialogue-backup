package com.microideation.app.dialogue.annotations;

import com.microideation.app.dialogue.event.EventStore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sandheepgr on 17/6/16.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface PublishEvent {

    public EventStore eventStore();
    public boolean isPersistent() default false;
    public String channelName();


}
