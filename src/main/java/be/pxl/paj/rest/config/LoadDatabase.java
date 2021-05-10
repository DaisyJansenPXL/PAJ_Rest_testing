package be.pxl.paj.rest.config;

import be.pxl.paj.rest.domain.Employee;
import be.pxl.paj.rest.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger LOGGER = LogManager.getLogger(LoadDatabase.class);
 // Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.
    // Worden eenmaal gerund bij het opstarten van de applicatie en wordt deze data opgeslagen in de in-memory db
  @Bean
  public CommandLineRunner initDatabase(EmployeeRepository repository) {
    return args -> {
      LOGGER.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
      LOGGER.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
    };
  }
}
