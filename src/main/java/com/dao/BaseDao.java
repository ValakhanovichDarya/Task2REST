package com.dao;

import com.configuration.ConnectionManager;
import com.model.entity.BaseEntity;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseDao<T extends BaseEntity> {

    ConnectionManager connectionManager;

    BaseDao() {
        connectionManager = new ConnectionManager();
    }

    BaseDao(PoolProperties poolProperties) {
        connectionManager = new ConnectionManager(poolProperties);
    }

    public abstract String getSelectQueryById();

    public abstract String getSelectAllQuery();

    public abstract String getCreateQuery();

    public List<T> findAll(){
        List<T> list = new ArrayList<>();
        try (ResultSet resultSet = connectionManager.getConnection()
                .prepareStatement(getSelectAllQuery()).executeQuery()) {
                    while (resultSet.next()) {
                        list.add(createFromResultSet(resultSet));
                    }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    Optional<T> findById(long id) {
        try (PreparedStatement preparedStatement = connectionManager.getConnection()
                .prepareStatement(getSelectQueryById())) {
                preparedStatement.setLong(1, id);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(createFromResultSet(resultSet));
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void create(T object) {
        try (PreparedStatement preparedStatement = connectionManager.getConnection()
                .prepareStatement(getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForCreate(preparedStatement, object);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                object.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void prepareStatementForCreate(PreparedStatement statement, T object) throws SQLException;

    public abstract T createFromResultSet(ResultSet resultSet) throws SQLException;

}
