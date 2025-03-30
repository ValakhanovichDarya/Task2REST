package com.dao;

import com.configuration.ConnectionManager;
import com.model.entity.Author;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao {
    private static final String AUTHORS_TABLE_NAME = "authors";
    private static AuthorDao INSTANCE = null;
    ConnectionManager connectionManager;

    private AuthorDao() {
        connectionManager = new ConnectionManager();
    }

    AuthorDao(PoolProperties poolProperties) {
        connectionManager = new ConnectionManager(poolProperties);
    }

    public static AuthorDao getInstance() {
        if (INSTANCE == null) {
            synchronized (AuthorDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AuthorDao();
                }
            }
        }
        return INSTANCE;
    }

    public List<Author> findAll() throws SQLException {
        List<Author> list = new ArrayList<>();
        ResultSet resultSet = connectionManager.getConnection()
                .prepareStatement("SELECT * FROM authors").executeQuery();

        while (resultSet.next())
            list.add(createFromResultSet(resultSet));
        return list;
    }

    Optional<Author> findById(long id) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection()
                .prepareStatement("SELECT * FROM authors WHERE id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) return Optional.of(createFromResultSet(resultSet));
        return Optional.empty();
    }

    public void create(Author object) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection()
                .prepareStatement("INSERT INTO authors (name) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, object.getName());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            object.setId(generatedKeys.getLong(1));
    }

    private Author createFromResultSet(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setName(resultSet.getString(AUTHORS_TABLE_NAME + ".name"));
        author.setId(resultSet.getLong(AUTHORS_TABLE_NAME + ".id"));
        return author;
    }

}
