package com.july;

<<<<<<< HEAD
import com.july.entity.Timeline;
import com.july.repository.Mongodb.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> master
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableOAuth2Sso
@EnableScheduling
public class JulyApplication {

<<<<<<< HEAD
	@Autowired
	private TimelineRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(JulyApplication.class, args);
	}

	//test mongodb connnect
	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of Timelines
		repository.save(new Timeline(1));
		repository.save(new Timeline(2));

		// fetch all Timelines
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Timeline customer : repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		/*System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Timeline customer : repository.findByLastName("Smith")) {
			System.out.println(customer);
		}*/
=======
    private static final Logger logger = LoggerFactory.getLogger(JulyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JulyApplication.class, args);
>>>>>>> master

        logger.info("July Web Service is Running !");
	}

}
