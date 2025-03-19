package com.jocata.bankingmgntsystem.um.controller;

import com.jocata.bankingmgntsystem.security.JWTService;
import com.jocata.bankingmgntsystem.um.form.SignInForm;
import com.jocata.bankingmgntsystem.um.form.UserForm;
import com.jocata.bankingmgntsystem.um.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserForm> registerUser(@RequestBody UserForm userForm) {

        UserForm user = userService.createUser(userForm);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody SignInForm signInForm) {

        UserForm userByEmailAndPass = userService.getUserByEmailAndPass(signInForm.getEmail(), signInForm.getPassword());
        String token = jwtService.generateToken(userByEmailAndPass.getEmail(),userByEmailAndPass.getRoles());
        return ResponseEntity.ok(Map.of("user", userByEmailAndPass, "token", token));

    }


    @GetMapping("/users/{id}")
    public ResponseEntity<UserForm> getUserById(@PathVariable Integer id) {

        UserForm user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @PostMapping("/admin/upload")
    public ResponseEntity<Map<String, Object>> createUsersFromFile(@RequestParam("file") MultipartFile file) {

        Map<String, Object> response = userService.createUsersFromFile(file);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserForm>> getAllUsers() {

        List<UserForm> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

}
