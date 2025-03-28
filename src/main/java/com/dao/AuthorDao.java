package com.dao;

import com.model.entity.Author;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDao extends BaseDao<Author> {
    private static final String AUTHORS_TABLE_NAME = "authors";
    private static AuthorDao INSTANCE = null;

    private AuthorDao() {
    }

    AuthorDao(PoolProperties poolProperties) {
        super(poolProperties);
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

    @Override
    public String getSelectQueryById() {
        return "SELECT * FROM authors WHERE id = ?";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM authors";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO authors (name) VALUES (?)";
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement statement, Author object) throws SQLException {
        statement.setString(1, object.getName());
    }


    @Override
    public Author createFromResultSet(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setName(resultSet.getString(AUTHORS_TABLE_NAME + ".name"));
        author.setId(resultSet.getLong(AUTHORS_TABLE_NAME + ".id"));
        return author;
    }

}
