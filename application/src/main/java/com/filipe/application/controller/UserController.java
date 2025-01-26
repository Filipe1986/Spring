package com.filipe.application.controller;



import com.filipe.application.entity.User;
import com.filipe.application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

     private final UserService userService;

     UserController(UserService userService) {
         this.userService = userService;
     }

     @GetMapping("/users")
     public ResponseEntity<List<?>> getUsers() {
         return ResponseEntity.ok().body(userService.getUsers());
     }

     @GetMapping("/users/{id}")
     public ResponseEntity<User> getUser(@PathVariable Long id) {
         return ResponseEntity.ok().body(userService.getUser(id));
     }

     @PostMapping("/users")
     public ResponseEntity<User> addUser(@RequestBody User user) {
         return ResponseEntity.ok().body(userService.addUser(user));
     }

     @PutMapping("/users/{id}")
     public ResponseEntity<User> updateUser(@PathVariable Long id, User user) {
         return ResponseEntity.ok().body(userService.updateUser(id, user));
     }

     @DeleteMapping("/users/{id}")
     public ResponseEntity<?> deleteUser(@PathVariable Long id) {

         userService.deleteUser(id);
         return ResponseEntity.ok().body("User deleted");

     }

}
