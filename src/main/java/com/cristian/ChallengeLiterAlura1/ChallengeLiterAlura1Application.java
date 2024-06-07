package com.cristian.ChallengeLiterAlura1;

import com.cristian.ChallengeLiterAlura1.principal.Principal;
import com.cristian.ChallengeLiterAlura1.repository.IRepositorioAutor;
import com.cristian.ChallengeLiterAlura1.repository.IRepositorioLibro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class ChallengeLiterAlura1Application implements CommandLineRunner {
	@Autowired
	private IRepositorioLibro libroRepository;
	@Autowired
	private IRepositorioAutor autorRepository;


	public static void main(String[] args) {

		SpringApplication.run(ChallengeLiterAlura1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository);
		principal.menu();

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		for (Thread t : threadSet) {
			System.out.println(t);


		}
	}
}

