package com.jocata.bankingmgntsystem.um.form;

import java.util.Set;

public class UserForm {

    private String userId;

    private String username;

    private String email;

    private String password;

    private String nationality;

    private Set<AddressForm> addresses;

    private Set<RoleForm> roles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<AddressForm> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressForm> addresses) {
        this.addresses = addresses;
    }

    public Set<RoleForm> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleForm> roles) {
        this.roles = roles;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
