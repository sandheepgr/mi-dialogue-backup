package com.microideation.app.dialogue;

import com.microideation.app.dialogue.service.SampleService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
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
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SampleService sampleService;



    @RequestMapping(path = "/load/{queuename}", method = RequestMethod.GET)
    public void loadData(@PathVariable(value = "queuename") String queuename) {

        if ( queuename.equals("test3"))
            sampleService.test();

    }

}