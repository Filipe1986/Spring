package com.filipe.scenarios;

import com.filipe.User;
import com.filipe.application.Application;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
class ApplicationTest {

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void configureAndCreateUsersTable() {
        // Initialize PostgreSQLContainer with PostGIS image
        PostgreSQLContainer<?> postgis = new PostgreSQLContainer<>(
                DockerImageName.parse("postgis/postgis:16-3.4-alpine")
                        .asCompatibleSubstituteFor("postgres")
        );

        // Configure container with password and port

        // Start the container
        postgis.start();

        try {
            // Create a connection to the database
            String jdbcUrl = postgis.getJdbcUrl();
            String username = postgis.getUsername();
            String password = postgis.getPassword();

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a 'users' table
            String createTableQuery = """
                    CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                    """;

            // Execute the query
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
                System.out.println("Users table created successfully.");
            }

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Test
    void getUser() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:"+ port +"/users/1";
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        User user = response.getBody();

        assert user != null;
        assert user.id() == 1L;
        assert user.name().equals("Filipe");
        assert user.email().equals("filipe@spring.com");

    }

    @Test
    void getUsers() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:"+ port +"/users";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

        List<?> users = response.getBody();

        assert users != null;
        assert users.size() == 2;

    }

}