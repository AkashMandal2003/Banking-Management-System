package com.jocata.bankingmgntsystem.um.dao;

import com.jocata.bankingmgntsystem.um.entity.Address;

import java.util.List;
import java.util.Set;

public interface AddressDao {

    Address createAddress(Address address);

    Address getAddress(Integer id);

    List<Address> getAddressByUserId(Integer userId);

    Address updateAddress(Address address);

    void deleteAddress(Integer addressId);

}
