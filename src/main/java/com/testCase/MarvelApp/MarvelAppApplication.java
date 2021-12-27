package com.testCase.MarvelApp;

import com.testCase.MarvelApp.Entity.Characters;
import com.testCase.MarvelApp.Entity.Comics;
import com.testCase.MarvelApp.Service.CharacterService;
import com.testCase.MarvelApp.Service.ComicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.Temporal;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MarvelAppApplication implements CommandLineRunner {

	@Autowired
	private CharacterService characterService;

	@Autowired
	private ComicsService comicsService;

	public static void main(String[] args) {
		SpringApplication.run(MarvelAppApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		Comics comics = new Comics("Avengers 2", 2.0, "Avengers", new Date(), 130, "URL",
				new HashSet<>() , 3.99f);

		Characters characters1 = characterService.getOneById(4L);
		Characters characters2 = characterService.getOneById(7L);
		comics.getCharactersSet().add(characters1);
		comics.getCharactersSet().add(characters2);

		characters1.getComicsSet().add(comics);
		characters2.getComicsSet().add(comics);
		comicsService.addComics(comics);
	}

}
