package com.library.api;

import com.library.db.entity.BookEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookAPITest {

    private static SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @BeforeAll
    public static void setUpClass() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(BookEntity.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    @DisplayName("Предварительная очистка таблицы")
    public void setUp() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.createQuery("DELETE FROM BookEntity").executeUpdate();
        transaction.commit();
        session.close();
    }

    @Test
    @DisplayName("Добавление двух записей в таблицу и получение созданных записей")
    public void testAddBooks() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        BookEntity book1 = new BookEntity().setBookTitle("Book 1").setUpdated(new Date());
        BookEntity book2 = new BookEntity().setBookTitle("Book 2").setUpdated(new Date());
        session.save(book1);
        session.save(book2);

        transaction.commit();

        List<BookEntity> books = session.createQuery("FROM BookEntity", BookEntity.class).list();
        System.out.println(books);
        assertEquals(2, books.size());

        session.close();
    }

    @Test
    @DisplayName("Получение записи по названию книги")
    public void testGetBookByTitle() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        BookEntity book = new BookEntity().setBookTitle("Book 1").setUpdated(new Date());
        session.save(book);
        transaction.commit();

        BookEntity foundBook = session.createQuery("FROM BookEntity WHERE bookTitle = :title", BookEntity.class)
                .setParameter("title", "Book 1")
                .uniqueResult();
        System.out.println(foundBook);
        assertNotNull(foundBook);
        assertEquals("Book 1", foundBook.getBookTitle());

        session.close();
    }

    @Test
    @DisplayName("Удаление записи из таблицы")
    public void testDeleteBook() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        BookEntity book = new BookEntity().setBookTitle("Book 1").setUpdated(new Date());
        session.save(book);
        transaction.commit();

        transaction = session.beginTransaction();
        session.delete(book);
        transaction.commit();

        List<BookEntity> books = session.createQuery("FROM BookEntity", BookEntity.class).list();
        System.out.println(books);
        assertEquals(0, books.size());

        session.close();
    }

    @Test
    @DisplayName("Получение оставшейся записи после удаления одной записи")
    public void testRemainingBookAfterDeletion() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        BookEntity book1 = new BookEntity().setBookTitle("Book 1").setUpdated(new Date());
        BookEntity book2 = new BookEntity().setBookTitle("Book 2").setUpdated(new Date());
        session.save(book1);
        session.save(book2);
        transaction.commit();

        transaction = session.beginTransaction();
        session.delete(book1);
        transaction.commit();

        List<BookEntity> books = session.createQuery("FROM BookEntity", BookEntity.class).list();
        System.out.println(books);
        assertEquals(1, books.size());
        assertEquals("Book 2", books.get(0).getBookTitle());

        session.close();
    }

    @AfterEach
    public void tearDown() {
        if (session != null) {
            session.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
