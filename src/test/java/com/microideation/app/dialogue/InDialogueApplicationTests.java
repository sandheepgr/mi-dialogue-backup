package com.microideation.app.dialogue;

import com.microideation.app.dialogue.service.SampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InDialogueApplication.class)
@WebAppConfiguration
public class InDialogueApplicationTests {

    @Autowired
    private SampleService sampleService;

	@Test
	public void contextLoads() {
	}

    @Test
    public void simpleTest() {

        sampleService.test();
        System.out.println("This is a test");

    }

}
