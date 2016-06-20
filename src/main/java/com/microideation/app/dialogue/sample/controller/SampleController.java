package com.microideation.app.dialogue.sample.controller;

import com.microideation.app.dialogue.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sandheepgr on 20/6/16.
 */

@RestController
@Profile("dev")
public class SampleController {

    @Autowired
    private SampleService sampleService;


    @RequestMapping(path = "/rabbit", method = RequestMethod.GET)
    public void publishToRabbit() {

        sampleService.publishToRabbit();

    }


    @RequestMapping(path = "/redis", method = RequestMethod.GET)
    public void publishToRedis() {

        sampleService.publishToRedis();

    }

}