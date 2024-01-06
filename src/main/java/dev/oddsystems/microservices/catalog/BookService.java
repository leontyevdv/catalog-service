package dev.oddsystems.microservices.catalog;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final Validator validator;

  @Autowired
  public BookService(BookRepository bookRepository, Validator validator) {
    this.bookRepository = bookRepository;
    this.validator = validator;
  }

  public Collection<Book> viewBookList() {
    return bookRepository.findAll();
  }

  public Book viewBookDetails(String isbn) {
    return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
  }

  public Book addBookToCatalog(Book book) {
    validateBook(book);
    if (bookRepository.existsByIsbn(book.isbn())) {
      throw new BookAlreadyExistsException(book.isbn());
    }
    return bookRepository.save(book);
  }

  public void removeBookFromCatalog(String isbn) {
    bookRepository.deleteByIsbn(isbn);
  }

  public Book editBookDetails(String isbn, Book book) {
    validateBook(book);
    return bookRepository
        .findByIsbn(isbn)
        .map(
            existingBook -> {
              var bookToUpdate =
                  new Book(existingBook.isbn(), book.title(), book.author(), book.price());
              return bookRepository.save(bookToUpdate);
            })
        .orElseGet(() -> addBookToCatalog(book));
  }

  private void validateBook(Book book) {
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
