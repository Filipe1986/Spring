package com.filipe.application.controller;



import com.filipe.User;
import com.filipe.application.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

     private final UserService userService;

     UserController(UserService userService) {
         this.userService = userService;
     }

     @GetMapping("/users")
     public List<User> getUsers() {
         return userService.getUsers();
     }

     @GetMapping("/users/{id}")
     public User getUser(@PathVariable Long id) {
         return userService.getUser(id);
     }

     @PostMapping("/users")
     public User addUser(User user) {
         return userService.addUser(user);
     }

     @PutMapping("/users/{id}")
     public User updateUser(@PathVariable Long id, User user) {
         return userService.updateUser(id, user);
     }

     @DeleteMapping("/users/{id}")
     public void deleteUser(@PathVariable Long id) {
         userService.deleteUser(id);
     }

}
