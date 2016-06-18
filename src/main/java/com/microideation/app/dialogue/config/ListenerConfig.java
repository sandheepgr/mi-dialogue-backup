package com.microideation.app.dialogue.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Created by sandheepgr on 18/6/16.
 */
@Order(value = Integer.MAX_VALUE)
@Configuration
@ComponentScan( basePackages = {"com.microideation.app.dialogue.handlers","com.microideation.app.dialogue.support"})
public class ListenerConfig {
}
