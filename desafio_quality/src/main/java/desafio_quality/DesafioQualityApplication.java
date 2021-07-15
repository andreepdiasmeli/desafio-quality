package desafio_quality;

import desafio_quality.services.DBService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioQualityApplication implements CommandLineRunner {

	private final DBService dbService;

	public DesafioQualityApplication(DBService dbService) {
		this.dbService = dbService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DesafioQualityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dbService.instantiateDB();
	}
}
