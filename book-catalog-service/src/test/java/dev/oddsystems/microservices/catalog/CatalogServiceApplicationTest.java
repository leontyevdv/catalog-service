package dev.oddsystems.microservices.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import dev.oddsystems.microservices.books.server.model.BookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTest {

  @Autowired private WebTestClient webTestClient;

  @Test
  void whenGetRequestWithIdThenBookReturned() {
    var bookIsbn = "1231231230";
    var bookToCreate = new BookDTO(bookIsbn, "Title", "Author", 9.90, 0);
    BookDTO expectedBook =
        webTestClient
            .post()
            .uri("/books")
            .bodyValue(bookToCreate)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BookDTO.class)
            .value(book -> assertThat(book).isNotNull())
            .returnResult()
            .getResponseBody();

    webTestClient
        .get()
        .uri("/books/" + bookIsbn)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(BookDTO.class)
        .value(
            actualBook -> {
              assertThat(actualBook).isNotNull();
              assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
            });
  }

  @Test
  void whenPostRequestThenBookCreated() {
    var expectedBook = new BookDTO("1231231231", "Title", "Author", 9.90, 0);

    webTestClient
        .post()
        .uri("/books")
        .bodyValue(expectedBook)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(BookDTO.class)
        .value(
            actualBook -> {
              assertThat(actualBook).isNotNull();
              assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
            });
  }

  @Test
  void whenPutRequestThenBookUpdated() {
    var bookIsbn = "1231231232";
    var bookToCreate = new BookDTO(bookIsbn, "Title", "Author", 9.90, 0);
    BookDTO createdBook =
        webTestClient
            .post()
            .uri("/books")
            .bodyValue(bookToCreate)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BookDTO.class)
            .value(book -> assertThat(book).isNotNull())
            .returnResult()
            .getResponseBody();

    var bookToUpdate =
        new BookDTO(
            createdBook.getIsbn(), createdBook.getTitle(), createdBook.getAuthor(), 7.95, 0);

    webTestClient
        .put()
        .uri("/books/" + bookIsbn)
        .bodyValue(bookToUpdate)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(BookDTO.class)
        .value(
            actualBook -> {
              assertThat(actualBook).isNotNull();
              assertThat(actualBook.getPrice()).isEqualTo(bookToUpdate.getPrice());
            });
  }

  @Test
  void whenDeleteRequestThenBookDeleted() {
    var bookIsbn = "1231231233";
    var bookToCreate = new BookDTO(bookIsbn, "Title", "Author", 9.90, 0);
    webTestClient
        .post()
        .uri("/books")
        .bodyValue(bookToCreate)
        .exchange()
        .expectStatus()
        .isCreated();

    webTestClient.delete().uri("/books/" + bookIsbn).exchange().expectStatus().isNoContent();

    webTestClient
        .get()
        .uri("/books/" + bookIsbn)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody(String.class)
        .value(
            errorMessage ->
                assertThat(errorMessage)
                    .isEqualTo("The book with ISBN " + bookIsbn + " was not found."));
  }
}
