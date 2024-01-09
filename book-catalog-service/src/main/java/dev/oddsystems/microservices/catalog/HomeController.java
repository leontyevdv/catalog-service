package dev.oddsystems.microservices.catalog;

import dev.oddsystems.microservices.catalog.config.OddBooksProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
  private final OddBooksProperties properties;

  public HomeController(OddBooksProperties properties) {
    this.properties = properties;
  }

  @GetMapping("/")
  public String getGreeting() {
    return properties.greeting();
  }
}
