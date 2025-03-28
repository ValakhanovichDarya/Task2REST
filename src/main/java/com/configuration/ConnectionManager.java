package com.configuration;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public final class ConnectionManager {

    private static DataSource dataSource;

    public ConnectionManager() {
        PoolProperties poolProperties = new PoolProperties();
        try {
            Properties prop = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
            assert inputStream != null;
            prop.load(inputStream);
            poolProperties.setDriverClassName(prop.getProperty("datasource.driver"));
            poolProperties.setUrl(prop.getProperty("datasource.url"));
            poolProperties.setUsername(prop.getProperty("datasource.username"));
            poolProperties.setPassword(prop.getProperty("datasource.password"));
        } catch (IOException ignored){}

        dataSource = new DataSource(poolProperties);
    }

    public ConnectionManager(PoolProperties poolProperties) {
        dataSource = new DataSource(poolProperties);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}