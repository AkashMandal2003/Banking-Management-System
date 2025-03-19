package com.jocata.bankingmgntsystem.um.dao.impl;

import com.jocata.bankingmgntsystem.config.HibernateConfig;
import com.jocata.bankingmgntsystem.um.dao.AddressDao;
import com.jocata.bankingmgntsystem.um.entity.Address;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class AddressDaoImpl implements AddressDao {

    private final HibernateConfig hibernateConfig;

    public AddressDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public Address createAddress(Address address) {
        return hibernateConfig.saveEntity(address);
    }

    @Override
    public Address getAddress(Integer id) {
        return hibernateConfig.findEntityById(Address.class, id);
    }

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        return hibernateConfig.findEntitiesByCriteria(Address.class,"id", userId);
    }

    @Override
    public Address updateAddress(Address address) {
        return hibernateConfig.updateEntity(address);
    }

    @Override
    public void deleteAddress(Integer addressId) {
        hibernateConfig.deleteEntity(Address.class,addressId);
    }
}
