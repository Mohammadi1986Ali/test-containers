package com.javalab.tutorial.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgresqlContainerTest {

    private static String dockerImageName;
    private String query;
    private static PostgreSQLContainer postgresContainer;

    @BeforeAll
    static void startup() {
        dockerImageName = "postgres:15.2";
        postgresContainer = new PostgreSQLContainer(dockerImageName);
        postgresContainer.start();
    }

    @Test
    public void givenQuery_whenSelectQueryExecuted_thenResultsReturned() throws SQLException {
        query = "SELECT 1";

        ResultSet resultSet = performQuery(postgresContainer, query);
        resultSet.next();
        int result = resultSet.getInt(1);

        assertEquals(1, result);
    }

    private ResultSet performQuery(PostgreSQLContainer postgres, String query) throws SQLException {
        String jdbcUrl = postgres.getJdbcUrl();
        String username = postgres.getUsername();
        String password = postgres.getPassword();
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        return conn.createStatement().executeQuery(query);
    }

    @AfterAll
    static void cleanup() {
        postgresContainer.stop();
    }
}
