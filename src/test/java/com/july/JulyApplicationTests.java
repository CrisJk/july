package com.july;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JulyApplication.class)
@WebAppConfiguration
public class JulyApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(JulyApplicationTests.class);

	@Test
	public void contextLoads() {
		logger.info("July Test is Running !");
	}

}
