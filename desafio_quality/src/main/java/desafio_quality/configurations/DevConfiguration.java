package desafio_quality.configurations;

import desafio_quality.services.DBService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfiguration implements CommandLineRunner {

    private final DBService dbService;

    public DevConfiguration(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void run(String... args) throws Exception {
        dbService.instantiateDB();
    }
}
