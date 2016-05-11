package com.july;

import com.july.entity.Timeline;
import com.july.repository.Mongodb.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
public class JulyApplication  implements CommandLineRunner {

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

	}

}
