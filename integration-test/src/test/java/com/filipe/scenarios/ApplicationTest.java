package com.filipe.scenarios;

import com.filipe.application.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
class ApplicationTest {

    @LocalServerPort
    private int port;

    @Test
    void getUsers() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:"+ port +"/users/1";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println(response.getBody());

        assert response.getBody() != null;


    }

}