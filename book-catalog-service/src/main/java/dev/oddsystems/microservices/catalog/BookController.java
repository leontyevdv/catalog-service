package dev.oddsystems.microservices.catalog;

import dev.oddsystems.microservices.books.server.api.BooksApi;
import dev.oddsystems.microservices.books.server.model.BookDTO;
import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;
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
    Iterable<Book> books = bookService.viewBookList();
    return ResponseEntity.ok(
        StreamSupport.stream(books.spliterator(), false)
            .map(it -> new BookDTO(it.isbn(), it.title(), it.author(), it.price(), it.version()))
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
    return ResponseEntity.ok(
        new BookDTO(book.isbn(), book.title(), book.author(), book.price(), book.version()));
  }

  @Override
  public ResponseEntity<BookDTO> updateBook(String isbn, BookDTO bookDTO) {
    Book book =
        Book.of(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPrice());

    Book bookInCatalog = bookService.editBookDetails(isbn, book);

    return ResponseEntity.ok()
        .body(
            new BookDTO(
                bookInCatalog.isbn(),
                bookInCatalog.title(),
                bookInCatalog.author(),
                bookInCatalog.price(),
                bookInCatalog.version()));
  }

  @Override
  public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
    Book book =
        Book.of(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPrice());

    Book bookInCatalog = bookService.addBookToCatalog(book);

    return ResponseEntity.created(URI.create("/books/" + bookInCatalog.isbn()))
        .body(
            new BookDTO(
                bookInCatalog.isbn(),
                bookInCatalog.title(),
                bookInCatalog.author(),
                bookInCatalog.price(),
                bookInCatalog.version()));
  }
}
