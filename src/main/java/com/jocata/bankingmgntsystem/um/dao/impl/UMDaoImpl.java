package com.jocata.bankingmgntsystem.um.dao.impl;

import com.jocata.bankingmgntsystem.config.HibernateConfig;
import com.jocata.bankingmgntsystem.um.dao.UMDao;
import com.jocata.bankingmgntsystem.um.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UMDaoImpl implements UMDao {

    private final HibernateConfig hibernateConfig;

    public UMDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public User createUser(User user) {
        return hibernateConfig.saveEntity(user);
    }

    @Override
    public User getUser(Integer id) {
        return hibernateConfig.findEntityById(User.class, id);
    }

    @Override
    public User findUserByEmailAndPass(String email, String password) {
        Map<String,Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return hibernateConfig.findEntityByMultipleCriteria(User.class,params);
    }

    @Override
    public List<User> getAllUsers() {
        return hibernateConfig.loadEntitiesByCriteria(User.class);
    }
}
