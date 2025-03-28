package com.dao;

import com.model.entity.Book;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDao extends BaseDao<Book> {
    private static final String BOOKS_TABLE_NAME = "books";
    private static BookDao INSTANCE = null;

    BookDao(PoolProperties poolProperties) {
        super(poolProperties);
    }

    private BookDao() { }

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

    @Override
    public String getSelectQueryById() {
        return "SELECT * FROM books WHERE id = ?";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM books";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO books (name, numberOfPages, authorId) VALUES (?, ?, ?)";
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, Book object) throws SQLException {
        statement.setString(1, object.getName());
        statement.setInt(2, object.getNumberOfPages());
        statement.setLong(3, object.getAuthor().getId());
    }


    @Override
    public Book createFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong(BOOKS_TABLE_NAME + ".id"));
        book.setName(resultSet.getString(BOOKS_TABLE_NAME + ".name"));
        book.setNumberOfPages(resultSet.getInt(BOOKS_TABLE_NAME + ".numberOfPages"));
        long authorId = resultSet.getLong(BOOKS_TABLE_NAME + ".authorId");
        if(AuthorDao.getInstance().findById(authorId).isPresent())
            book.setAuthor(AuthorDao.getInstance().findById(authorId).get());
        return book;
    }

    public List<Book> findByAuthorId(long authorId) {
        List<Book> books = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from books where books.authorId=?")) {
                preparedStatement.setLong(1, authorId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        books.add(createFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void update(Book book){
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE books SET name = ?, numberOfPages = ?, authorId = ?  WHERE id = ?; ")) {
                preparedStatement.setString(1, book.getName());
                preparedStatement.setInt(2, book.getNumberOfPages());
                preparedStatement.setLong(3, book.getAuthor().getId());
                preparedStatement.setLong(4, book.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Book book){
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM books WHERE id = ?; ")) {
                preparedStatement.setLong(1, book.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
