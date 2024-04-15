package com.todotic.contactlistapi;

import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.repository.ContactRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ContactlistapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactlistapiApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(ContactRepository contactRepository){
		return args -> {
				List<Contact> contacts = Arrays.asList(
						new Contact("Santiago","santiaguiris@gmail.com", LocalDateTime.now()),
						new Contact("Esteban","estebin@gmail.com", LocalDateTime.now()),
						new Contact("Eduardo","eduardio@gmail.com", LocalDateTime.now()),
						new Contact("Javier","javierpa@gmail.com", LocalDateTime.now()),
						new Contact("Julian","julianchis@gmail.com", LocalDateTime.now())
				);
				contactRepository.saveAll(contacts);
		};
	}
}
