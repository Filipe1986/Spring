package com.filipe.application.service;

import com.filipe.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public List<User> getUsers() {
        return List.of(new User(1L, "Filipe", "filipe@spring.com"), new User(2L, "Paola", "filipe@spring.com"));
    }

    public User getUser(Long id) {
        return new User(1L, "Filipe", "filipe@spring.com" );
    }

    public User addUser(User user) {
        return null;
    }

    public User updateUser(Long id, User user) {
        return null;
    }

    public void deleteUser(Long id) {
    }
}
