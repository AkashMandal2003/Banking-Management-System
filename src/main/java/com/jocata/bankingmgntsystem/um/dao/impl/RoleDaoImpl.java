package com.jocata.bankingmgntsystem.um.dao.impl;

import com.jocata.bankingmgntsystem.config.HibernateConfig;
import com.jocata.bankingmgntsystem.um.dao.RoleDao;
import com.jocata.bankingmgntsystem.um.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final HibernateConfig hibernateConfig;

    public RoleDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return hibernateConfig.findEntityByCriteria(Role.class,"name",roleName);
    }
}
