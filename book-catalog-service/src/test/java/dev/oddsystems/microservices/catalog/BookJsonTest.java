package dev.oddsystems.microservices.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import dev.oddsystems.microservices.books.server.model.BookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class BookJsonTest {
  @Autowired private JacksonTester<BookDTO> json;

  @Test
  void testSerialize() throws Exception {
    var book = new BookDTO("1234567890", "Title", "Author", 9.90, 0);
    var jsonContent = json.write(book);
    assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.getIsbn());
    assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.getTitle());
    assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.getAuthor());
    assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.getPrice());
    assertThat(jsonContent).extractingJsonPathNumberValue("@.version").isEqualTo(book.getVersion());
  }

  @Test
  void testDeserialize() throws Exception {
    var content =
        """
        {
          "isbn": "1234567890",
          "title": "Title",
          "author": "Author",
          "price": 9.90,
          "version": 0
        }""";

    assertThat(json.parse(content))
        .usingRecursiveComparison()
        .isEqualTo(new BookDTO("1234567890", "Title", "Author", 9.90, 0));
  }
}
