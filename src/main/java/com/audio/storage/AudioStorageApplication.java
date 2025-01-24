package com.audio.storage;

import com.audio.storage.entity.Phrase;
import com.audio.storage.entity.User;
import com.audio.storage.repository.PhraseRepository;
import com.audio.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AudioStorageApplication {

	public static void main(String[] args) {

		SpringApplication.run(AudioStorageApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;
	@Autowired
	PhraseRepository phraseRepository;
	@Bean
	public CommandLineRunner runner() {
		//create repositories

		return args -> {
			if(userRepository.findAll().isEmpty()) {
				userRepository.save(new User(null,"admin", "admin@gmail.com"));
			}
			if(phraseRepository.findAll().isEmpty()) {
				phraseRepository.save(new Phrase(null, "hello"));
			}
		};

	}

}
