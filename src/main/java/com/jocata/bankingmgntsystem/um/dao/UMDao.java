package com.jocata.bankingmgntsystem.um.dao;

import com.jocata.bankingmgntsystem.um.entity.User;

import java.util.List;

public interface UMDao {

    User createUser(User user);

    User getUser(Integer id);

    User findUserByEmailAndPass(String email, String password);

    List<User> getAllUsers();

}
