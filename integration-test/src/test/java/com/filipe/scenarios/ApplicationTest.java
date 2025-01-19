package com.filipe.scenarios;

import com.filipe.User;
import com.filipe.application.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
class ApplicationTest {

    @LocalServerPort
    private int port;

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