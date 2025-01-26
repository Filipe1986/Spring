package com.filipe.scenarios;

import com.filipe.application.Application;
import com.filipe.application.entity.User;
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
        ); // Retrieve dynamic port

        // Configure container with password and port
        postgis.withExposedPorts(5432);

        // Start the container
        postgis.start();

        try {
            // Create a connection to the database
            String jdbcUrl = postgis.getJdbcUrl();
            String username = postgis.getUsername();
            String password = postgis.getPassword();

            System.setProperty("spring.datasource.url", postgis.getJdbcUrl());
            System.setProperty("spring.datasource.username", postgis.getUsername());
            System.setProperty("spring.datasource.password", postgis.getPassword());

            System.out.println("JDBC URL: " + jdbcUrl + " Username: " + username + " Password: " + password);

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
    void addUser(){

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:"+ port +"/users";
        User user = new User();
        user.setEmail("john@email.com");
        user.setName("John");




        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);

        User newUser = response.getBody();

        System.out.println(newUser);

        assert newUser != null;
    }


    @Test
    void getUser() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:"+ port +"/users/1";
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        User user = response.getBody();

        assert user != null;
        assert user.getEmail().equals("john@email.com");


    }

    @Test
    void getUsers() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:"+ port +"/users";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

        List<?> users = response.getBody();

        assert users != null;
        assert users.size() == 1;

    }

}