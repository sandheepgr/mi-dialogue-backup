package com.microideation.app.dialogue;

import com.microideation.app.dialogue.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class InDialogueApplication {

	public static void main(String[] args) {


		SpringApplication.run(InDialogueApplication.class, args);

	}

}




@RestController
class Controller {

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