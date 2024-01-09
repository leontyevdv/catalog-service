package dev.oddsystems.microservices.catalog;

import dev.oddsystems.microservices.books.server.api.BooksApi;
import dev.oddsystems.microservices.books.server.model.BookDTO;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class BookController implements BooksApi {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @Override
  public ResponseEntity<List<BookDTO>> getBooks() {
    return ResponseEntity.ok(
        bookService.viewBookList().stream()
            .map(it -> new BookDTO(it.isbn(), it.title(), it.author(), it.price()))
            .toList());
  }

  @Override
  public ResponseEntity<Void> deleteBook(String isbn) {
    bookService.removeBookFromCatalog(isbn);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<BookDTO> getBook(String isbn) {
    Book book = bookService.viewBookDetails(isbn);
    return ResponseEntity.ok(new BookDTO(book.isbn(), book.title(), book.author(), book.price()));
  }

  @Override
  public ResponseEntity<BookDTO> updateBook(String isbn, BookDTO bookDTO) {
    Book book =
        new Book(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPrice());

    Book bookInCatalog = bookService.editBookDetails(isbn, book);

    return ResponseEntity.ok()
        .body(
            new BookDTO(
                bookInCatalog.isbn(),
                bookInCatalog.title(),
                bookInCatalog.author(),
                bookInCatalog.price()));
  }

  @Override
  public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
    Book book =
        new Book(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPrice());

    Book bookInCatalog = bookService.addBookToCatalog(book);

    return ResponseEntity.created(URI.create("/books/" + bookInCatalog.isbn()))
        .body(
            new BookDTO(
                bookInCatalog.isbn(),
                bookInCatalog.title(),
                bookInCatalog.author(),
                bookInCatalog.price()));
  }
}
