package com.jocata.bankingmgntsystem.um.service;

import com.jocata.bankingmgntsystem.um.form.UserForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserForm createUser(UserForm userForm);

    Map<String, Object> createUsersFromFile(MultipartFile file);

    UserForm getUserById(Integer id);

    UserForm getUserByEmailAndPass(String email, String password);

    List<UserForm> getAllUsers();
}
