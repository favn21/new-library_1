package com.library.service;

import static com.library.utils.DateValidator.getDateString;
import static java.util.Objects.nonNull;

import com.library.api.model.getbooks.AuthorResponse;
import com.library.api.model.saveauthor.AuthorSaveRequest;
import com.library.api.model.saveauthor.AuthorSaveResponse;
import com.library.api.model.getbooks.AuthorWsdlRequest;
import com.library.api.model.getbooks.BookListResponse;
import com.library.api.model.getbooks.BookResponse;
import com.library.api.model.saveauthor.BookSaveRequest;
import com.library.api.model.savebook.BookSaveResponse;
import com.library.api.model.ValidationErrorResponse;
import com.library.api.model.exceptions.CustomBadRequestException;
import com.library.api.model.exceptions.CustomServiceErrorException;
import com.library.db.entity.AuthorEntity;
import com.library.db.entity.BookEntity;
import com.library.db.repository.AuthorRepository;
import com.library.db.repository.BookRepository;
import com.library.utils.DateValidator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  AuthorRepository authorRepository;

  public BookListResponse getAllBooksByAuthor(AuthorWsdlRequest request) {
    return new BookListResponse().setBook(getAllBooksByAuthor(request.getAuthorId()));
  }

  public List<BookResponse> getAllBooksByAuthor(long authorId) {

    List<BookResponse> books = new ArrayList<>();

    if (authorRepository.findById(authorId).isEmpty()) {
      throw new CustomBadRequestException(
          new ValidationErrorResponse("1004", "Указанный автор не существует в таблице"));
    }

    log.info("Поиск книг по указанному автору");
    try {
      List<BookEntity> bookEntities = bookRepository.findAllByAuthor_Id(authorId);
      log.info("Найдено книг: " + bookEntities.size());

      for (BookEntity book : bookEntities) {
        AuthorEntity author = book.getAuthor();
        books.add(new BookResponse(book.getBookTitle(), book.getUpdated(),
            new AuthorResponse(author.getId(), author.getFirstName(), author.getSecondName(),
                author.getFamilyName(), getDateString(author.getBirthDate()))));
      }

    } catch (Exception e) {
      throw new CustomBadRequestException(
          new ValidationErrorResponse("1005", "Ошибка получения данных"));
    }

    return books;
  }

  public AuthorSaveResponse saveAuthor(AuthorSaveRequest request) {
    Date parsedDate = null;

    if (nonNull(request.getBirthDate())) {
      parsedDate = DateValidator.isValidDate(request.getBirthDate());
    }

    AuthorEntity author = new AuthorEntity();

    Optional<AuthorEntity> authorEntities = authorRepository.findByFirstNameAndFamilyName(
        request.getFirstName(), request.getFamilyName());
    log.info("Выполнен поиск автора: ".concat(authorEntities.toString()));

    if (authorEntities.isPresent()) {
      log.info("Автор уже найден");
      throw new CustomBadRequestException(
          new ValidationErrorResponse("1002", "Указанный автор уже добавлен в базу данных"));
    }

    try {
      log.info("Сохранение автора.");
      author
          .setFirstName(request.getFirstName())
          .setSecondName(request.getSecondName())
          .setFamilyName(request.getFamilyName())
          .setBirthDate(parsedDate);

      authorRepository.save(author);
      return new AuthorSaveResponse(author.getId());

    } catch (Exception e) {
      throw new CustomServiceErrorException(
          new ValidationErrorResponse("1003", "Ошибка сохранения данных"));
    }
  }

  public BookSaveResponse saveBooks(BookSaveRequest book) {

    BookEntity bookEntity = new BookEntity();

    Optional<AuthorEntity> author = authorRepository.findById(book.getAuthor().getId());

    if (author.isEmpty()) {
      throw new CustomBadRequestException(
          new ValidationErrorResponse("1004", "Указанный автор не существует в таблице"));
    }

    try {
      log.info("Сохранение книги.");
      bookEntity
          .setBookTitle(book.getBookTitle())
          .setAuthor(author.get())
          .setUpdated(new Date());

      bookRepository.save(bookEntity);

    } catch (Exception e) {
      throw new CustomServiceErrorException(
          new ValidationErrorResponse("1003", "Ошибка сохранения данных: ".concat(e.getMessage())));
    }

    return new BookSaveResponse(bookEntity.getId());
  }
}