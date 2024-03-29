package dev.oddsystems.microservices.catalog.demo;

import dev.oddsystems.microservices.catalog.Book;
import dev.oddsystems.microservices.catalog.BookRepository;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "oddbooks.testdata.enabled", havingValue = "true")
public class BookDataLoader {

  private final BookRepository bookRepository;

  public BookDataLoader(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void loadBookTestData() {
    bookRepository.deleteAll();

    var book1 = Book.of("1234567891", "Northern Lights", "Lyra Silverstar", 9.90);
    var book2 = Book.of("1234567892", "Polar Journey", "Iorek Polarson", 12.90);

    bookRepository.saveAll(List.of(book1, book2));
  }
}
