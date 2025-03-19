package com.jocata.bankingmgntsystem.um.service.impl;

import com.jocata.bankingmgntsystem.um.dao.AddressDao;
import com.jocata.bankingmgntsystem.um.dao.RoleDao;
import com.jocata.bankingmgntsystem.um.dao.UMDao;
import com.jocata.bankingmgntsystem.um.entity.Address;
import com.jocata.bankingmgntsystem.um.entity.Role;
import com.jocata.bankingmgntsystem.um.entity.User;
import com.jocata.bankingmgntsystem.um.form.AddressForm;
import com.jocata.bankingmgntsystem.um.form.RoleForm;
import com.jocata.bankingmgntsystem.um.form.UserForm;
import com.jocata.bankingmgntsystem.um.service.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UMDao umDao;
    private final RoleDao roleDao;
    private final AddressDao addressDao;

    public UserServiceImpl(UMDao umDao, RoleDao roleDao, AddressDao addressDao) {
        this.umDao = umDao;
        this.roleDao = roleDao;
        this.addressDao = addressDao;
    }

    @Override
    public UserForm createUser(UserForm userForm) {

        User user = userFormToUser(userForm);
        User savedUser = umDao.createUser(user);

        //If address available
        Set<Address> addresses = addAddressesToUser(userForm.getAddresses(), savedUser);

        return userToUserForm(savedUser);
    }


    @Override
    public Map<String, Object> createUsersFromFile(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<UserForm> createdUsers = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<UserForm> users = parseUsersFromExcel(sheet, errors);
            createdUsers = saveUsers(users, errors);

            response.put("createdUsers", createdUsers);
            response.put("errors", errors);
        } catch (Exception e) {
            response.put("error", "Failed to read Excel file: " + e.getMessage());
        }
        return response;
    }

    private List<UserForm> parseUsersFromExcel(Sheet sheet, List<String> errors) {
        List<UserForm> users = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            try {
                UserForm userForm = new UserForm();
                userForm.setUsername(row.getCell(0).getStringCellValue());
                userForm.setEmail(row.getCell(1).getStringCellValue());
                userForm.setPassword(row.getCell(2).getStringCellValue());
                userForm.setNationality(row.getCell(4).getStringCellValue());

                // Parsing addresses
                String addressData = row.getCell(5).getStringCellValue();
                Set<AddressForm> addresses = parseAddresses(addressData);
                userForm.setAddresses(addresses);

                // Parsing roles
                String rolesData = row.getCell(6).getStringCellValue();
                Set<RoleForm> roles = parseRoles(rolesData);
                userForm.setRoles(roles);

                users.add(userForm);
            } catch (Exception e) {
                errors.add("Error in row " + row.getRowNum() + ": " + e.getMessage());
            }
        }
        return users;
    }


    @Override
    public UserForm getUserById(Integer id) {
        User user = umDao.getUser(id);
        return userToUserForm(user);
    }

    @Override
    public UserForm getUserByEmailAndPass(String email, String password) {
        User userByEmailAndPass = umDao.findUserByEmailAndPass(email, password);
        return userToUserForm(userByEmailAndPass);
    }

    @Override
    public List<UserForm> getAllUsers() {
        List<User> allUsers = umDao.getAllUsers();
        List<UserForm> userForms = new ArrayList<>();
        for (User user : allUsers) {
            userForms.add(userToUserForm(user));
        }
        return userForms;
    }

    private User userFormToUser(UserForm userForm) {

        User user = new User();
        user.setName(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setRoles(getRolesForUser(userForm));
        user.setNationality(userForm.getNationality());

        return user;
    }

    private Set<Role> getRolesForUser(UserForm userForm) {

        Set<Role> roles = new HashSet<>();
        if (userForm.getRoles() == null || userForm.getRoles().isEmpty()) {
            Role defaultRole = roleDao.getRoleByName("USER");
            roles.add(defaultRole);
        } else {
            for (RoleForm roleform : userForm.getRoles()) {
                Role role = roleDao.getRoleByName(roleform.getRoleName());
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    private Set<Address> addAddressesToUser(Set<AddressForm> addressForms, User user) {

        Set<Address> addresses = new HashSet<>();

        if (addressForms != null && !addressForms.isEmpty()) {
            for (AddressForm addressForm : addressForms) {
                Address address = new Address();
                address.setStreet(addressForm.getStreet());
                address.setCity(addressForm.getCity());
                address.setState(addressForm.getState());
                address.setZipCode(addressForm.getZipCode());
                address.setCountry(addressForm.getCountry());
                address.setUser(user);

                Address savedAddress = addressDao.createAddress(address);
                addresses.add(savedAddress);
            }
        }
        return addresses;
    }

    private UserForm userToUserForm(User user) {

        UserForm userForm = new UserForm();
        userForm.setUserId(user.getId().toString());
        userForm.setUsername(user.getName());
        userForm.setEmail(user.getEmail());
        userForm.setNationality(user.getNationality());

        Set<RoleForm> roleForms = getRoleForms(user);
        userForm.setRoles(roleForms);

        Set<AddressForm> addressForms = getAddressForms(user);
        userForm.setAddresses(addressForms);

        return userForm;

    }

    private Set<AddressForm> getAddressForms(User user) {

        Set<AddressForm> addressForms = new HashSet<>();
        List<Address> addressByUserId = addressDao.getAddressByUserId(user.getId());
        for (Address address : addressByUserId) {
            AddressForm addressForm = new AddressForm();
            addressForm.setStreet(address.getStreet());
            addressForm.setCity(address.getCity());
            addressForm.setState(address.getState());
            addressForm.setZipCode(address.getZipCode());
            addressForm.setCountry(address.getCountry());
            addressForms.add(addressForm);
        }
        return addressForms;

    }

    private Set<RoleForm> getRoleForms(User user) {

        Set<RoleForm> roleForms = new HashSet<>();
        for (Role role : user.getRoles()) {
            RoleForm roleForm = new RoleForm();
            roleForm.setRoleName(role.getName());
            roleForms.add(roleForm);
        }
        return roleForms;

    }

    private List<UserForm> saveUsers(List<UserForm> users, List<String> errors) {

        List<UserForm> createdUsers = new ArrayList<>();
        for (UserForm userForm : users) {
            try {
                UserForm savedUser = createUser(userForm);
                createdUsers.add(savedUser);
            } catch (Exception e) {
                errors.add("Failed to save user: " + userForm.getEmail() + " - " + e.getMessage());
            }
        }
        return createdUsers;
    }

    private Set<AddressForm> parseAddresses(String addressData) {

        Set<AddressForm> addresses = new HashSet<>();
        String[] addressArray = addressData.split(";");
        for (String address : addressArray) {
            String[] parts = address.split(",");
            if (parts.length == 5) {
                AddressForm addressForm = new AddressForm();
                addressForm.setStreet(parts[0].trim());
                addressForm.setCity(parts[1].trim());
                addressForm.setState(parts[2].trim());
                addressForm.setZipCode(parts[3].trim());
                addressForm.setCountry(parts[4].trim());
                addresses.add(addressForm);
            }
        }
        return addresses;

    }

    private Set<RoleForm> parseRoles(String rolesData) {

        Set<RoleForm> roles = new HashSet<>();
        String[] roleArray = rolesData.split(",");
        for (String role : roleArray) {
            RoleForm roleForm = new RoleForm();
            roleForm.setRoleName(role.trim());
            roles.add(roleForm);
        }
        return roles;
    }

}
