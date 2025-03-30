package com.dao;

import com.configuration.ConnectionManager;
import com.model.entity.Book;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao {
    private static final String BOOKS_TABLE_NAME = "books";
    private static BookDao INSTANCE = null;
    ConnectionManager connectionManager;

    BookDao(PoolProperties poolProperties) {
        connectionManager = new ConnectionManager(poolProperties);
    }

    private BookDao() {
        connectionManager = new ConnectionManager();
    }

    public static BookDao getInstance() {
        if (INSTANCE == null) {
            synchronized (BookDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookDao();
                }
            }
        }
        return INSTANCE;
    }

    public List<Book> findAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        ResultSet resultSet = connectionManager.getConnection()
                .prepareStatement("SELECT * FROM books").executeQuery();

        while (resultSet.next())
            list.add(createFromResultSet(resultSet));
        return list;
    }

    public Optional<Book> findById(long id) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection()
                .prepareStatement("SELECT * FROM books WHERE id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next())
            return Optional.of(createFromResultSet(resultSet));
        return Optional.empty();
    }

    public void create(Book object) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection()
                .prepareStatement("INSERT INTO books (name, numberOfPages, authorId) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
        prepareStatementForCreate(preparedStatement, object);
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            object.setId(generatedKeys.getLong(1));
    }

    private void prepareStatementForCreate(PreparedStatement statement, Book object) throws SQLException {
        statement.setString(1, object.getName());
        statement.setInt(2, object.getNumberOfPages());
        statement.setLong(3, object.getAuthor().getId());
    }

    private Book createFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong(BOOKS_TABLE_NAME + ".id"));
        book.setName(resultSet.getString(BOOKS_TABLE_NAME + ".name"));
        book.setNumberOfPages(resultSet.getInt(BOOKS_TABLE_NAME + ".numberOfPages"));
        long authorId = resultSet.getLong(BOOKS_TABLE_NAME + ".authorId");
        if (AuthorDao.getInstance().findById(authorId).isPresent())
            book.setAuthor(AuthorDao.getInstance().findById(authorId).get());
        return book;
    }

    public List<Book> findByAuthorId(long authorId) throws SQLException {
        List<Book> books = new ArrayList<>();
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(
                "select * from books where books.authorId=?");
        preparedStatement.setLong(1, authorId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
            books.add(createFromResultSet(resultSet));
        return books;
    }

    public void update(Book book) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(
                "UPDATE books SET name = ?, numberOfPages = ?, authorId = ?  WHERE id = ?; ");
        preparedStatement.setString(1, book.getName());
        preparedStatement.setInt(2, book.getNumberOfPages());
        preparedStatement.setLong(3, book.getAuthor().getId());
        preparedStatement.setLong(4, book.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(Book book) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(
                "DELETE FROM books WHERE id = ?; ");
        preparedStatement.setLong(1, book.getId());
        preparedStatement.executeUpdate();
    }

}
