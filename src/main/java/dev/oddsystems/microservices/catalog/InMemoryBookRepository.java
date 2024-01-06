package dev.oddsystems.microservices.catalog;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookRepository implements BookRepository {

  private final Map<String, Book> books = new ConcurrentHashMap<>();

  public InMemoryBookRepository() {
    books.put(
        "978-1-56619-909-4",
        new Book(
            "978-1-56619-909-4",
            "The C Programming Language",
            "Brian W. Kernighan, Dennis M. Ritchie",
            22.99));
    books.put(
        "978-0-13-235088-4",
        new Book(
            "978-0-13-235088-4",
            "Clean Code: A Handbook of Agile Software Craftsmanship",
            "Robert C. Martin",
            37.99));
  }

  @Override
  public Collection<Book> findAll() {
    return books.values();
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    return books.get(isbn) != null;
  }

  @Override
  public Book save(Book book) {
    books.put(book.isbn(), book);
    return book;
  }

  @Override
  public void deleteByIsbn(String isbn) {
    books.remove(isbn);
  }
}
